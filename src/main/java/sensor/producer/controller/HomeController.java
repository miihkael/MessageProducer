package sensor.producer.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import sensor.producer.data.DSessionData;
import sensor.producer.data.Sensor;
import sensor.producer.data.SensorForm;
import sensor.producer.domain.MessageService;
import sensor.producer.service.MyMessagingService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;
import java.io.*;
import java.util.Iterator;
import java.util.Map;

import static sensor.producer.data.DSessionData.maxMessagingCount;

/**
 * Created by mika on 29.5.2016.
 */
@Controller("homeController")
@SessionAttributes({"sessionData"})
public class HomeController {

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private ServletContext servletContext;

    @Autowired
    MessageService messageService;

    @Autowired
    MyMessagingService myMessagingService;

    //@Autowired                            // Enable if localization is desired.
    //private MessageSource messageSource;

    private volatile DSessionData mainSessionData = null;


    // See root.xml for the PostConstruct bean.
    // This is run after any constructors and sertters, but before anything else.
    @PostConstruct
    public void initProducer() throws Exception {
        logger.debug("initProducer(): CREATE started");

        String rootPath = servletContext.getRealPath("/");
        System.out.println(rootPath);

        mainSessionData = new DSessionData();
        boolean fRes = createSensors(mainSessionData, rootPath);


        // mainSessionData.setExecutor();     // This calls: xyz = Executors.newFixedThreadPool(maxThreadCount);
        logger.debug("CREATE finished");
    }


    // This is run just before the class is destroyed.
    // NOTICE: The application must be close by Exit !!!, not by Stop !!!!
    @PreDestroy
    public void cleanProducer() throws Exception {
        logger.debug("cleanProducer(): DESTROY started");
        /*for (Thread worker : saveWorkers) {
            worker.setRunning(false);
        }

        // Now join with each thread to make sure we give them time to stop gracefully
        for (Thread worker : saveWorkers) {
            worker.join();  // May want to use the one that allows a millis for a timeout
        }*/
        if (mainSessionData != null) {
            mainSessionData.Destroy();
        }
        /*saveWorkers.shutdown();
        while (!saveWorkers.isTerminated()) { }*/
        logger.debug("cleanProducer(): DESTROY finished");
    }


    @RequestMapping(value = {"", "/", "/home"}, method = RequestMethod.GET)
    public String home(Model model)
    {
        logger.debug("home(GET): STARTED");
        // Fetch the sensors information from the DSessionData and make a SensorForm
        // to pass all the sensors at once into the jsp form.

        SensorForm sensorForm = new SensorForm();
        /*if (mainSessionData != null) {
            sensorForm.setSensorsInForm(mainSessionData.getSensors());
        }
        model.addAttribute("sensorForm", sensorForm);*/

        if (!model.containsAttribute("sessionData")) {
            model.addAttribute("sessionData", mainSessionData);
            sensorForm.setSensorsInForm(mainSessionData.getSensors());
        }
        else {
            DSessionData sesData = (DSessionData)fetchSessionData(model, "sessionData");
            if (sesData != null) {
                sensorForm.setSensorsInForm(sesData.getSensors());
            }
        }
        model.addAttribute("sensorForm", sensorForm);

        /*
        // Testing pojo:
        MessagePojo mess = new MessagePojo("Sensori12345", ACC);
        System.out.println("MessagePojo directly from string: " + mess.toString());

        //StringWriter stringer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new StringWriter(), mess);    // Create object mapper.

            // Read back from Objectmapper:
            ObjectMapper submapper = new ObjectMapper();
            Map<String, Double> map = new HashMap<String, Double>();
            // convert JSON string to Map
            map = submapper.readValue(mess.getData(), new TypeReference<Map<String, String>>(){});

            System.out.println("MessagePojo from ObjectMapper (from getData()): " + map);   // This is better...
            // This will show subdata incorrectly if it contain json string:
            System.out.println("MessagePojo from ObjectMapper (as string): " + mapper.writeValueAsString(mess));
        }
        catch (IOException ioe) {
            System.out.println("MessagePojo to ObjectMapper failed: " + ioe.getMessage());
        }
        // Testing pojo ends.
        */

        logger.debug("home(GET): ENDED");
        return "/home";
    }


    @RequestMapping(value = {"", "/", "/home"}, method = RequestMethod.POST)
    public String home(Model model,
                       @ModelAttribute("sensorForm") SensorForm sensorForm,
                       BindingResult bindingResult,
                       @RequestParam("actionbutton") String strButton )
    {

        //String strFullFile = null;
        boolean fError = false;
        DSessionData sesData = null;

        // Get the localized strings from property-files (according locale): ****************
        //Locale locale = LocaleContextHolder.getLocale();
        //String strStartText = messageSource.getMessage("xyz.label.startmessaging", null, locale);
        //String strReloadText = messageSource.getMessage("xyz.label.reloadmessaging", null, locale);
        // **********************************************************************************
        //String strStartText = "start";
        String strReloadText = "reload";

        // If there is any problems to use 'mainSessionData' directly, you can get
        // it from session also by calling 'fetchSessionData' method (see below).
        if (model.containsAttribute("sessionData")) {
            sesData = (DSessionData)fetchSessionData(model, "sessionData");
        }
        else {
            sesData = mainSessionData;          // Questionable usage   ???
        }

        if (strButton.equals(strReloadText)) {
            // User has selected reload for the sensors data:
            String rootPath = servletContext.getRealPath("/");
            logger.debug("home(POST): reloading from '" + rootPath + "'!");

            mainSessionData.deleteActiveSensors();

            mainSessionData = new DSessionData();
            createSensors(mainSessionData, rootPath);
            model.addAttribute("sessionData", mainSessionData);

            return "redirect:/home";
        }

        if (sensorForm == null) {
            bindingResult.addError(new ObjectError("usererror", "No sensor data available."));
            System.out.println("home(POST): ERROR: Invalid sensor data: Enter valid sensor seeds into 'SensorProducerSensorList.dat'!");
            fError = true;
        }

        // Now merge the SensorForm into the mainSessionData:
        int iThreads = mergeFormToSession(sensorForm);
        if (iThreads < 1) {
            bindingResult.addError(new ObjectError("usererror", "No messaging selected."));
            System.out.println("home(POST): ERROR: No messaging selected: Select at least one sensor to be messaging!");
            fError = true;
        }
        if (iThreads > maxMessagingCount) {
            bindingResult.addError(new ObjectError("usererror", "Too many messaging selected."));
            System.out.println("home(POST): ERROR: Too many messaging selected: Drop out selected sensors to maximum of " + maxMessagingCount + "!");
            fError = true;
        }

        if (fError) {
            return "/home";
        }

        try {
           sesData.setMessageService(messageService);
           myMessagingService.startMessaging(sesData);
        }
        catch (Exception e) {
            //logger.
            return "/home";
        }
        return "redirect:/producing";
    }


    @RequestMapping(value={"/producing"}, method = RequestMethod.GET)
    public String startSending(Model model) {

        SensorForm sensorForm = new SensorForm();
        DSessionData sesData = null;

        if (model.containsAttribute("sessionData")) {
            sesData = (DSessionData)fetchSessionData(model, "sessionData");
        }
        else {
            sesData = mainSessionData;          // Questionable usage   ???
        }

        sensorForm.setSensorsInForm(mainSessionData.getActiveSensors());
        model.addAttribute("sensorForm", sensorForm);

        return "/producing";
    }


    @RequestMapping(value={"/producing"}, method = RequestMethod.POST)
    public String stopSending(Model model) {

        // This handles 'Return' button and stops message sending before leaving /producing form.
        // Usage ModelAttribute above if you want to change something in form, like stopping individual threads...

        // TODO: Also implement the browser back button functionality.. or brower closure or page closure/change...?

        DSessionData sesData = null;

        if (model.containsAttribute("sessionData")) {
            sesData = (DSessionData)fetchSessionData(model, "sessionData");
        }
        else {
            sesData = mainSessionData;          // Questionable usage   ???
        }

        try {
            myMessagingService.stopMessaging(sesData);
        }
        catch (Exception e) {
            //logger.
            return "/home";
        }

        return "redirect: /home";
    }


    // =====================================================================================

    /*
    This method fetches the sessionData currently stored as session object in
    model. The desired object name is passed as parameter on 'strAttribString'.
    E.g. to fetch 'sessionData' (main data set for this application, call:
    DSessionData sesData = (DSessionData) fetchSessionData(model, "sessionData");
    */
    private Object fetchSessionData(Model model, String strAttribString) {
        if (model == null || strAttribString.isEmpty()) {
            return null;
        }

        Map<String, Object> sesContext = model.asMap();
        Iterator entries = sesContext.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry currEntry = (Map.Entry) entries.next();
            String key = (String)currEntry.getKey();
            if (key.equals(strAttribString)) {
                return currEntry.getValue();
            }
        }
        return null;
    }


    /*
    This method tests if the given file (as parameter) exists and can be opened.
    */
    private boolean testFile(String filePath) {
        boolean fRet = false;
        File f = new File(filePath);
        if (f.exists()) {
            FileInputStream in = null;
            try {
                // Open the stream.
                in = new FileInputStream(f);
                fRet = true;
                // If we get here, we can close also!
                in.close();
            }
            catch (IOException ioe) {
                fRet = false;
            }
        }
        return fRet;
    }


    /*
    This method reads through file 'SensorProducerSensorList.dat' containing list of
    sensors. Each sensor (one line in the file) is stored in the passed DSessionData
    object to be used by the application. Parameter 'strRootPath' contains the location
    of the source file (should be root folder of the application).
    */
    private boolean createSensors(DSessionData sessionData, String strRootPath) {
        // Read sensorid's from the external file into the DSessionData...
        boolean fRet = true;
        final String strAppName = "SensorProducer";                 // Name of us!
        final String strFileName = "SensorProducerSensorList.dat";  // Name of the sensorId file.

        if ( sessionData == null || strRootPath.isEmpty() ) {
            System.out.println("HomeContorller::createSensors: Invalid parameters passed to create!");
            return false;
        }

        int iPosEnd = strRootPath.lastIndexOf(strAppName);
        String strFilePath = strRootPath.substring(0, iPosEnd + strAppName.length() + 1);
        String strFullName = strFilePath + strFileName;
        BufferedReader br = null;
        boolean fRead = true;
        StringBuffer strRet = new StringBuffer();

        try {
            // Clear all existing data:
            sessionData.deleteActiveSensors();
            sessionData.deleteSensors();

            sessionData.setBasePath(strFilePath);           // Store for 'FILE' type handling.

            br = new BufferedReader(new FileReader(strFullName));
            sessionData.deleteSensors();

            while (fRead) {
                try {
                    String strLine;

                    if ( (strLine = br.readLine()) != null ) {
                        sessionData.addSensor(strLine); //  // strLine contains whole base sensor!
                            //fRet = false;
                            //fRead = false;      // Stop the creating even if one fails!
                        //}
                    }
                    else {
                        fRead = false;
                    }
                }
                catch (IOException ioe) {
                    fRead = false;
                    fRet = false;
                    ioe.printStackTrace();
                }
            }
        }
        catch (IOException ioe) {
            fRet = false;
            ioe.printStackTrace();
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        System.out.println(sessionData.toString());
        return fRet;
    }


    // Merge user selections and changes from SensorForm object into
    // main session data object.
    // If no sensor is selected for messaging, return zero. Otherwise the number of
    // threads to start.
    private Integer mergeFormToSession(SensorForm sensorForm) {
        int iRet = 0;

        if (mainSessionData == null || sensorForm == null || sensorForm.getSensorsInForm().size() == 0) {
            return iRet;
        }

        for ( Sensor mainSensor : mainSessionData.getSensors() ) {
            if (mainSensor.isSelected()) {
                mainSensor.setSelected(false);
            }

            // Find matching sensor among sensor list of the form and mark mainSessionData according selection status:
            for ( Sensor sensor : sensorForm.getSensorsInForm() ) {
                if (mainSensor.getSensorId().equals(sensor.getSensorId())) {
                    if (sensor.isSelected()) {
                        mainSensor.setSelected(true);

                        // Also set the timeout:
                        mainSensor.setTimeOut(sensor.getTimeOut());
                        iRet++;
                        break;  // To the next item in main loop...
                    }
                }
            }
        }

        return iRet;
    }

}
