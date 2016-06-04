package sensor.producer.data;

import sensor.producer.controller.Producer;
import sensor.producer.domain.MessageService;

import java.io.File;

/**
 * Created by mika on 2.6.2016.
 */
public class DSessionData {

    private String strSensorId;
    private String strMsgType;
    private String strMsgFile;  // This contains (finally) the canonical path of the file and is used for operations.
    private boolean fSending;
    private MessageService messageService;  // Passing message service to the message thread.
    private Producer runnable;  // Needed for thread closing.
    private Thread thread;      // Needed for thread closing.
    private File msgFile;       // Needed just to get the canonical path of the file.

    public DSessionData() {

        this.fSending = false;
        this.messageService = null;
        this.runnable = null;
        this.thread = null;
        msgFile = null;
    }
    public DSessionData(String strSensId, String strMsgType, String strMsgFile, boolean fSending) {
        this.strSensorId = strSensId;
        this.strMsgType = strMsgType;
        this.strMsgFile = strMsgFile;
        this.fSending = false;
        messageService = null;
        runnable = null;
        thread = null;
        msgFile = null;
    }

    public String getStrSensorId() {
        return strSensorId;
    }

    public void setStrSensorId(String strSensorId) {
        this.strSensorId = strSensorId;
    }

    public String getStrMsgType() {
        return strMsgType;
    }

    public void setStrMsgType(String strMsgType) {
        this.strMsgType = strMsgType;
    }

    public String getStrMsgFile() {
        return strMsgFile;
    }

    public void setStrMsgFile(String strMsgFile) {
        this.strMsgFile = strMsgFile;
    }

    public boolean isfSending() {
        return fSending;
    }

    public void setfSending(boolean fSending) {
        this.fSending = fSending;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
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
        fSending = true;
    }

    public File getMsgFile() {
        return msgFile;
    }

    public void setMsgFile(File msgFile) {
        this.msgFile = msgFile;
    }
}
