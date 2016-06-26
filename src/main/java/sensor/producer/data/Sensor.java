package sensor.producer.data;

import sensor.producer.controller.Producer;

import java.io.File;

/**
 * Created by mika on 17.6.2016.
 */
public class Sensor {
    private String sensorId;        // Sensor id.
    private DSessionData.SensorType sensorType;
    private Integer timeOut;        // Sending timeout.

    private Producer runnable;      // Needed for thread closing.
    private Producer thread;        // Messaging thread.
    private String messageFile;     // Message file, if sensor type is 'FILE'.
    private File msgFile;           // Needed just to get the canonical path of the file ('FILE' type only).

    private boolean selected;       // 'true' if selected as messaging object.
    private StringBuilder strStatus;    // Status of this sensor messaging (show on messaging form).


    public Sensor() {
        this.sensorId = "<Not initialised";
        this.sensorType = DSessionData.SensorType.NONE;
        this.timeOut = DSessionData.defaultTimeOut;
        this.runnable = null;
        this.thread = null;
        this.msgFile = null;
        this.selected = false;
        this.strStatus = new StringBuilder();
    }

    public Sensor(String sensId) {
        this.sensorId = sensId;
        this.sensorType = DSessionData.SensorType.NONE;
        this.timeOut = DSessionData.defaultTimeOut;
        this.runnable = null;
        this.thread = null;
        this.msgFile = null;
        this.selected = false;
        this.strStatus = new StringBuilder();
    }

    public String getSensorId() {
        return sensorId;
    }
    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public DSessionData.SensorType getSensorType() {
        return sensorType;
    }
    public void setSensorType(DSessionData.SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public Integer getTimeOut() {
        return timeOut;
    }
    public void setTimeOut(Integer timeOut) {
        if (timeOut < DSessionData.minTimeOut) {
            this.timeOut = DSessionData.minTimeOut;
        }
        else if (timeOut > DSessionData.maxTimeOut) {
            this.timeOut = DSessionData.maxTimeOut;
        }
        else {
            this.timeOut = timeOut;
        }
    }

    public Producer getRunnable() {
        return runnable;
    }
    public void setRunnable(Producer runnable) {
        this.runnable = runnable;
    }

    public Producer getThread() {
        return thread;
    }
    public void setThread(Producer thread) {
        this.thread = thread;
    }

    public String getMessageFile() {
        return messageFile;
    }
    public void setMessageFile(String messageFile) {
        this.messageFile = messageFile;
    }

    public File getMsgFile() {
        return msgFile;
    }
    public void setMsgFile(File msgFile) {
        this.msgFile = msgFile;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getStrStatus() {
        return strStatus.toString();
    }
    public void setStrStatus(String strStatus) {
        this.strStatus.setLength(0);
        this.strStatus.append(strStatus);
    }
}
