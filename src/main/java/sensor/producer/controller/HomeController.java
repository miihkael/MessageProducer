package sensor.producer.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import sensor.producer.data.DSessionData;
import sensor.producer.domain.MessageService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by mika on 29.5.2016.
 */
@Controller
@SessionAttributes({"sessionData"})
public class HomeController {

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    MessageService messageService;

    @RequestMapping(value = {"", "/", "/home"}, method = RequestMethod.GET)
    public String home(Model model)
    {
        if (!model.containsAttribute("sessionData")) {
            model.addAttribute("sessionData", new DSessionData());
            model.addAttribute("msgtype", "Integeri");

            // TODO: the same for the msgFile also...?
        }
        else {
            DSessionData sesData = (DSessionData)fetchSessionData(model, "sessionData");
            if (sesData != null) {
                model.addAttribute("msgtype", sesData.getStrMsgType());
            }
        }

        return "/home";
    }


    @RequestMapping(value = {"", "/", "/home"}, method = RequestMethod.POST)
    public String home(Model model,
                       //@ModelAttribute("sensorID") String strSensorId,
                       @ModelAttribute("msgtype") String strMsgType,
                       //@ModelAttribute("msgfile") String strMsgFile )
                       //@ModelAttribute("msgfile") File msgFile,
                       @ModelAttribute("sessionData") DSessionData sesData,
                       BindingResult bindingResult)
    {

        //String strFullFile = null;

        if (sesData == null || sesData.getStrSensorId().isEmpty()) {
            bindingResult.addError(new ObjectError("usererror", "Sensor ID must be given!"));
            return "/home";
        }
        if (strMsgType.isEmpty()) {
            bindingResult.addError(new ObjectError("usererror", "Message type must be selected!"));
            return "/home";
        }
        else {
            sesData.setStrMsgType(strMsgType);
        }
        if (sesData.getStrMsgType().equals("Filei")) {
            if (sesData.getMsgFile() != null) {
                try {
                    sesData.setStrMsgFile(sesData.getMsgFile().getCanonicalPath());

                    // Still try to open the file to be sure:
                    if (!testFile(sesData.getStrMsgFile())) {
                        bindingResult.addError(new ObjectError("usererror", "Selected file cannot be found or accessed!"));
                        return "/home";
                    }
                }
                catch (IOException ioe) {
                    logger.info("File selection failed!");
                    bindingResult.addError(new ObjectError("usererror", "Selected file cannot be found or accessed!"));
                    return "/home";
                }
            } else {
                bindingResult.addError(new ObjectError("usererror", "Message file must be selected from server file system!"));
                return "/home";
            }
        }

        try {
            sesData.setMessageService(messageService);

            Producer runnable = new Producer(sesData);
            Thread thread = new Thread(runnable);

            sesData.setThread(thread);      // Store thread so it is possible to termiante it later on!
            // Also sets the running flag to true!
            sesData.setRunnable(runnable);

            thread.start();
            model.addAttribute("sessionData", sesData);
        }
        catch (Exception e) {
            //logger.
            return "/home";
        }
        return "/producing";
    }


    @RequestMapping(value={"/producing"}, method = RequestMethod.GET)
    public String startSending(Model model, @ModelAttribute("sessionData") DSessionData sessionData) {

        return "/producing";
    }


    @RequestMapping(value={"/producing"}, method = RequestMethod.POST)
    public String stopSending(Model model, @ModelAttribute("sessionData") DSessionData sessionData,
                              SessionStatus sessionStatus) {

        // This handles 'Return' button and stops message sending before leaving /producing form.

        // TODO: Also implement the browser back button functionality.. or brower closure or page closure/change...?

        if (sessionData != null) {
            if (sessionData.isfSending()) {
                Thread thread = sessionData.getThread();
                if (thread != null) {
                    Producer runnable = sessionData.getRunnable();
                    if (runnable != null) {
                        runnable.Terminate();
                        sessionData.setfSending(false);
                        try {
                            thread.join();
                        }
                        catch (InterruptedException ie) {
                            System.out.println(ie.getMessage());
                            logger.info("Sending thread closing: Exception!");
                        }
                    }
                }
            }
        }
        //sessionStatus.setComplete();

        return "/home";
    }


    // =====================================================================================

// ${sessionData.strSensorId}
    // ${sessionData.msgFile}

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

    // Just to test if file exists and is valid for opening:
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
}
