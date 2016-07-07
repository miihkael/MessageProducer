package sensor.producer.data;

import sensor.producer.controller.Producer;
import sensor.producer.domain.MessageService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mika on 2.6.2016.
 *
 * This is session parameter storage hosting.
 */
public class DSessionData {

    private List<Sensor> sensors;         // List of available sensors:
                                          // This data is stored on application start folder in a file
                                          // 'SensorProducerSensorList.dat'. The number of possible message
                                          // threads must be based on the count of these sensors.

    private StringBuilder basePath;  // Canonical path of application (folder where all data files are located).

    private MessageService messageService;      // Passing message service to the message thread.

    private int activeSensCount;                // Number of sensor threads running.
    private ArrayList<Sensor> activeSensors;    // List of sensors that are messaging (thread is running).



    public DSessionData() {
        this.sensors = new ArrayList<>();
        this.basePath = new StringBuilder();
        this.messageService = null;
        this.activeSensCount = 0;
        this.activeSensors = new ArrayList<>();
    }

    public void Destroy() { }

    public List<Sensor> getSensors() {
        return sensors;
    }
    public void setSensors(List<Sensor> sens) {
        this.sensors = sens;
    }
    public boolean addSensor(String strSensorLine) {
        boolean fRet = false;
        if (!strSensorLine.isEmpty()) {
            Sensor sensor = ParseFromSensorLine(strSensorLine);
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

    public String getBasePath() {
        return basePath.toString();
    }
    public void setBasePath(String strBasePath) {
        this.basePath.setLength(0);
        this.basePath.append(strBasePath);
    }

    // ===================================================================================================

    private Sensor ParseFromSensorLine(String strLine) {
        // Sensor line can be like:
        // 'Sensori22222,TMP'
        // where fields are (separated by comma): sensorId or ApiKey,SENSORDATATYPE (,file name (for type 'FILE'))

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

        // Locate commas from the string and parse all fields:
        while (fCont) {
            iCommaPos = strLine.indexOf(iCh, iCurrPos); // Find 'iCh' from iCurrPos position onward.
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
                case 1:     // sensor data type
                    if (sensor != null) {
                        sensor.setSensorDataType(SENSORDATATYPE.valueOf(strItem.toString()));
                    }
                    else {
                        fCont = false;
                    }
                    break;
                case 2:     // message data file name
                    if (sensor != null) {
                        sensor.setMessageFile(strItem.toString());
                    }
                    else {
                        fCont = false;
                    }
                    break;
            }

            iLoopCount++;
            iCurrPos = iCommaPos + 1;
            iCommaPos = 0;

            //System.out.println(strItem.toString());

            strItem.setLength(0);
        }

        return sensor;
    }


    // =====================================================================================

    /*public enum SensorType {
        NONE,
        INTEGER,
        DECIMAL,
        FILE,
        BOOLEAN
    }*/

    public enum SENSORDATATYPE {
        NONE,       // Unspecified
        ACC,        // Accleration, 1...-1 (x, y, z)
        ALT,        // Altitude, m
        BAR,        // Barometer, hPa
        FLAG,       // on/off, boolean
        GPS,        // gps: lat, lon, alt
        GYR,        // Gyroscope, 1...-1 (x,y,z)
        HUM,        // Humidity, %
        LUX,        // Lightning, lux
        MAG,        //              , 1...-1 (x,y,z)
        TIMESTAMP,  // Timestap, double
        TMP        // Temperature, Celcius
    }

    public enum SENSORDATATYPE_SUB {
        LAT,        // Latitude,    Signed gegrees format; DDD,dddd: 90...-90
        LON,        // Longitude,   Signed gegrees format; DDD,dddd: 180...-180
        X,          // x-axis
        Y,          // y-axis
        Z           // z-axis
    }

    public static final int defaultTimeOut = 500;
    public static final int minTimeOut = 300;
    public static final int maxTimeOut = 10000;
    public static final int maxMessagingCount = 20;
}
