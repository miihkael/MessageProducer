package sensor.producer.data;

import sensor.producer.controller.Producer;
import sensor.producer.domain.MessageService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mika on 2.6.2016.
 *
 * This is session parameter storage hosting.
 * It provides all necessary parameters for thread so that they can access the
 * application (spring) datasystem. It also hosts the thread pool used for sending messages.
 */
public class DSessionData {

    //private StringBuilder strSensorId;

    private List<Sensor> sensors;         // List of available sensors:
                                          // This data is stored on application start folder in a file
                                          // 'SensorProducerSensorList.dat'. The number of possible message
                                          // threads must based on the count of these sensors.

    //private String strMsgType;
    private StringBuilder basePath;  // Canonical path of application (folder where all data files are located).
    //private boolean fSending;

/*    private boolean fIntType;       // true, if integer messaging is selected.
    private boolean fIntSending;    // true, if integer type is sending.
    private boolean fDecType;       // true, if decimal messaging is selected.
    private boolean fDecSending;    // true, if decimal type is sending.
    private boolean fFileType;      // true, if file messaging is selected.
    private boolean fFileSending;   // true, if file type is sending.

    private int intThreadNbr;       // Number of interger messaging threads ( 1 (default) - 3 ).
    private int decThreadNbr;       // Numberof decimal messaging threads ( --""-- ).

    private int intTimeOut;         // Timeout for integer messaging (300 - 10000 ms, default 500 ms).
    private int decTimeOut;         // Timeout for decimal messaging ( --""-- ).
    private int fileTimeOut;        // Timeout for file messaging ( --""-- ).
*/
    private MessageService messageService;  // Passing message service to the message thread.
/*    private Producer runnable;      // Needed for thread closing.
    private Thread thread;          // Needed for thread closing.
    private File msgFile;           // Needed just to get the canonical path of the file.
*/

    private int activeSensCount;     // Number of sensor threads running.
    private ArrayList<Sensor> activeSensors;    // List of sensors that are messaging (thread is running).



    public DSessionData() {
        this.sensors = new ArrayList<>();
        this.basePath = new StringBuilder();
        this.messageService = null;
        this.activeSensCount = 0;
        this.activeSensors = new ArrayList<>();


        //this.fSending = false;
        //this.strSensorId = new StringBuilder();
 /*       this.arrSensorIds = new ArrayList<>();

        this.strMsgFile = new StringBuilder();
        this.strMsgFile = null;
        this.fIntType = false;
        this.fIntSending = false;
        this.fDecType = false;
        this.fDecSending = false;
        this.fFileType = false;
        this.fFileSending = false;
        this.intThreadNbr = this.defaultThreadCount;
        this.decThreadNbr = this.defaultThreadCount;
        this.intTimeOut = this.defaultTimeOut;
        this.decTimeOut = this.defaultTimeOut;
        this.fileTimeOut = this.defaultTimeOut;
        this.messageService = null;
        this.runnable = null;
        this.thread = null;
        this.msgFile = null;
        this.workerPoolSize = this.maxThreadCount * 3;
        this.executor = null;
*/

    }

    public void Destroy() {
     //   TerminateRunnables();
        //TerminateExecutor();
    }




    public List<Sensor> getSensors() {
        return sensors;
    }
    public void setSensors(List<Sensor> sens) {
        this.sensors = sens;
    }
    public boolean addSensor(String strSensorId) {
        boolean fRet = false;
        if (!strSensorId.isEmpty()) {
            Sensor sensor = ParseFromSensorLine(strSensorId);
            if (sensor != null) {
                sensors.add(sensor);
                fRet = true;
            }
        }
        return fRet;
    }
    public Sensor getSensor(Integer iPos) {
        // Position is based on zero-index!!!!
        if (iPos >= 0 && iPos < sensors.size()) {
            return sensors.get(iPos);
        }
        return null;
    }
    public boolean deleteSensor(Integer iPos) {
        // Position is based on zero-index!!!!

        if (iPos >= 0 && iPos < sensors.size()) {
            return sensors.remove(iPos);
        }
        return false;
    }
    public void deleteSensors() {
        if ( activeSensors.size() > 0 ) {
            // TODO: terminate active messaging...

            // Stop possible messaging and delete all active sensors:
            deleteActiveSensors();
        }
        sensors.clear();
    }

    public MessageService getMessageService() {
        return messageService;
    }
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }


    public int getActiveSensCount() {
        return activeSensCount;
    }


    public ArrayList<Sensor> getActiveSensors() {
        return activeSensors;
    }
    public Sensor getAciveSensor(Integer iPos) {
        if (iPos >= 0 && iPos < activeSensors.size()) {
            return activeSensors.get(iPos);
        }
        return null;
    }
    public void addActiveSensor(Sensor sensor) {
        if (sensor != null) {
            this.activeSensors.add(sensor);
            activeSensCount++;
        }
    }
    public void deleteActiveSensors() {
        for (Sensor sensor : activeSensors) {
            if (sensor != null) {
                Producer prod = sensor.getThread();
                if (prod != null) {
                    prod.Terminate();

                    try {
                        prod.join();
                    }
                    catch (InterruptedException ie) {
                        System.out.println(ie.getMessage());
                    }

                    sensor.setThread(null);
                    activeSensCount--;
                }
            }
        }
        this.activeSensors.clear();
    }

    /*
    public ArrayList<String> getArrSensorIds() {
        return arrSensorIds;
    }

    //public void setArrSensorIds(ArrayList<String> arrSensorIds) {
    //    this.arrSensorIds = arrSensorIds;
    //}
    public void addSensorId(String strSensorId) {
        if (!strSensorId.isEmpty()) {
            this.arrSensorIds.add(strSensorId);
        }
    }
*/
    /*public String getStrSensorId() {
        return strSensorId.toString();
    }

    public void setStrSensorId(String strSensorId) {
        this.strSensorId.setLength(0);
        this.strSensorId.append(strSensorId);
    }
*/
    /*public String getStrMsgType() {
        return strMsgType;
    }
    public void setStrMsgType(String strMsgType) {
        this.strMsgType = strMsgType;
    }*/

    public String getBasePath() {
        return basePath.toString();
    }
    public void setBasePath(String strBasePath) {
        this.basePath.setLength(0);
        this.basePath.append(strBasePath);
    }

    /*public boolean isfSending() {
        return fSending;
    }
    public void setfSending(boolean fSending) {
        this.fSending = fSending;
    }*/
/*
    public boolean isfIntType() {
        return fIntType;
    }

    public void setfIntType(boolean fIntType) {
        this.fIntType = fIntType;
    }

    public boolean isfIntSending() {
        return fIntSending;
    }

    public void setfIntSending(boolean fIntSending) {
        this.fIntSending = fIntSending;
    }

    public boolean isfDecType() {
        return fDecType;
    }

    public void setfDecType(boolean fDecType) {
        this.fDecType = fDecType;
    }

    public boolean isfDecSending() {
        return fDecSending;
    }

    public void setfDecSending(boolean fDecSending) {
        this.fDecSending = fDecSending;
    }

    public boolean isfFileType() {
        return fFileType;
    }

    public void setfFileType(boolean fFileType) {
        this.fFileType = fFileType;
    }

    public boolean isfFileSending() {
        return fFileSending;
    }

    public void setfFileSending(boolean fFileSending) {
        this.fFileSending = fFileSending;
    }

    public int getIntThreadNbr() {
        return intThreadNbr;
    }

    public void setIntThreadNbr(int intThreadNbr) {
        this.intThreadNbr = intThreadNbr;
    }

    public int getDecThreadNbr() {
        return decThreadNbr;
    }

    public void setDecThreadNbr(int decThreadNbr) {
        this.decThreadNbr = decThreadNbr;
    }

    public int getIntTimeOut() {
        return intTimeOut;
    }

    public void setIntTimeOut(int intTimeOut) {
        this.intTimeOut = intTimeOut;
    }

    public int getDecTimeOut() {
        return decTimeOut;
    }

    public void setDecTimeOut(int decTimeOut) {
        this.decTimeOut = decTimeOut;
    }

    public int getFileTimeOut() {
        return fileTimeOut;
    }

    public void setFileTimeOut(int fileTimeOut) {
        this.fileTimeOut = fileTimeOut;
    }

    public Producer getRunnable() {
        return runnable;
    }

    public void setRunnable(Producer runnable) {
        this.runnable = runnable;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
        //fSending = true;
    }

    public File getMsgFile() {
        return msgFile;
    }

    public void setMsgFile(File msgFile) {
        this.msgFile = msgFile;
    }

    public int getDefaultThreadCount() {
        return defaultThreadCount;
    }

    public int getMaxThreadCount() {
        return maxThreadCount;
    }
*/
    public int getDefaultTimeOut() {
        return defaultTimeOut;
    }

    public int getMinTimeOut() {
        return minTimeOut;
    }

    public int getMaxTimeOut() {
        return maxTimeOut;
    }


 /*   public ExecutorService getExecutor() {
        return executor;
    }
*/
 /*   public void setExecutor() {
        TerminateExecutor();

        this.executor = Executors.newFixedThreadPool(this.workerPoolSize);
    }

    public void addRunnable(Runnable newRunnable) {
        if (newRunnable != null) {
            activeMessagers.add(newRunnable);
        }
    }

    public void terminateRunnables() {
        for (Runnable runnable : activeMessagers ) {
            if (runnable != null) {
              //  runnable.Terminate();
            }
        }
    }
*/
    // ===========================================================
/*    private void TerminateExecutor() {
        if (this.executor != null) {
            this.executor.shutdown();

            while (!executor.isTerminated()) {};
        }
        this.executor = null;
    }
*/
    private Sensor ParseFromSensorLine(String strLine) {
        // Sensor line can be like:
        // Sensori22222,File,testi.pickme
        // == sensorId,sensor type,file name (for type 'FILE')

        Sensor sensor = null;
        int iCh = ',';          // Delimiter.
        int iCommaPos = 0;
        int iCurrPos = 0;
        int iLoopCount = 0;
        StringBuilder strItem = new StringBuilder();
        boolean fCont = true;

        if (strLine.isEmpty() || strLine.startsWith("#")) {
            return null;       // Is empty or is comment line...
        }

        // First parse out the sensor line:
        while (fCont) {

            iCommaPos = strLine.indexOf(iCh, iCurrPos);
            if (iCommaPos > 0) {
                strItem.append(strLine.substring(iCurrPos, iCommaPos));
            }
            else {
                if (iCommaPos < strLine.length()) {
                    strItem.append(strLine.substring(iCurrPos));
                }
                fCont = false;
            }

            switch (iLoopCount) {
                case 0:     // sensorId
                    sensor = new Sensor(strItem.toString());
                    break;
                case 1:     // sensor type
                    if (sensor != null) {
                        sensor.setSensorType(SensorType.valueOf(strItem.toString()));
                    }
                    else {
                        fCont = false;
                    }
                    break;
                case 2:
                    if (sensor != null) {
                        sensor.setMessageFile(strItem.toString());
                    }
                    else {
                        fCont = false;
                    }
                    break;  // message data file name
            }

            iLoopCount++;
            iCurrPos = iCommaPos + 1;
            iCommaPos = 0;

            System.out.println(strItem.toString());

            strItem.setLength(0);
        }

        return sensor;
    }


    // =====================================================================================

    //private volatile List<String> sensorTypes = Arrays.asList("NONE", "INTEGER", "DECIMAL", "FILE", "BOOLEAN");
    public enum SensorType {
        NONE,
        INTEGER,
        DECIMAL,
        FILE,
        BOOLEAN
    }

    public static final int defaultTimeOut = 500;
    public static final int minTimeOut = 300;
    public static final int maxTimeOut = 10000;
    public static final int maxMessagingCount = 20;
}
