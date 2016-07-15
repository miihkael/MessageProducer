package sensor.producer.controller;


import sensor.producer.domain.MessageService;
import sensor.producer.domain.messages.MessageGenerator;

public class Producer extends Thread {
    private MessageGenerator messageGenerator;
    private MessageService messageService;
    private int iMsgTimeOut;
    private int iThreadId;
    private final int iMsgTimeOutError = 300;
    private boolean fRunning;

    public Producer(MessageService msgServ, MessageGenerator msgGener, Integer iMsgTimeOut, Integer iThrId) {
        this.messageService = msgServ;
        this.messageGenerator = msgGener;
        this.iMsgTimeOut = iMsgTimeOut;
        this.iThreadId = iThrId;
        this.fRunning = false;
    }

    public void Terminate() {
        this.fRunning = false;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Starting thread: " + iThreadId);
        processSending();
        System.out.println(Thread.currentThread().getName() + " End thread: " + iThreadId);
    }

    /* Message data contains three items separated by comma:
        1. ApiKey/sensorId
        2. Message creation time stamp.
        3. Message data
       Message is created by message generator...
    */
    private void processSending() {

        if ( this.messageService != null && this.messageGenerator != null ) {
            fRunning = true;
            while (fRunning) {
                try {
                    String strMessage = this.messageGenerator.getNextMessage();
                    if (!strMessage.isEmpty()) {
                        String reply = messageService.send(strMessage);
                        System.out.println(reply);
                        //System.out.println("Message: " + strMessage);
                    }
                    else {
                        System.out.println("Empty message!!!!!!!");
                    }
                }
                catch (Exception e) {
                    System.out.println("Sending failed: " + e.getMessage().toString());

                    //logger.info("error in message sending...");
                    try {
                        Thread.sleep(iMsgTimeOutError);
                        Thread.sleep(this.iMsgTimeOut);
                    }
                    catch (Exception se) {

                    }

                    continue;   // There may be no listeners, so just continue sending...
                }

                try {
                    Thread.sleep(this.iMsgTimeOut);
                }
                catch (Exception e) {

                }
            }

            // This will close the message file if generator type is 'FILE'.
            messageGenerator.closeMessaging();
        }
        else {
            System.out.println(Thread.currentThread().getName() + " Failed: Message Service or Message Generator is invalid!");
        }
    }
}
