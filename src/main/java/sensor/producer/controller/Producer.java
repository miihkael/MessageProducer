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
        1. ApiKey
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
                        //logger.info("Reply: " + reply);
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

                        // TODO: set up user passable sending timeout!
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
        }
        else {
            System.out.println(Thread.currentThread().getName() + " Failed: Message Service or Message Generator is invalid!");
        }
    }
}


/*
public class Producer implements Runnable {
    private MessageGenerator messageGenerator;
    private MessageService messageService;
    private int iMsgTimeOut;
    private final int iMsgTimeOutError = 300;
    private boolean fRunning;

    public Producer(MessageService msgServ, MessageGenerator msgGener, Integer iMsgTimeOut) {
        this.messageGenerator = msgGener;
        this.messageService = msgServ;
        this.iMsgTimeOut = iMsgTimeOut;
        this.fRunning = false;
    }

    public void Terminate() {
        this.fRunning = false;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Start");
        processSending();
        System.out.println(Thread.currentThread().getName() + " End");
    }


    private void processSending() {

        if ( this.messageService != null && this.messageGenerator != null ) {
            fRunning = true;
            while (fRunning) {
                try {
                    String strMessage = this.messageGenerator.getNextMessage();
                    if (!strMessage.isEmpty()) {
                        String reply = messageService.send(strMessage);
                        System.out.println(reply);
                        //logger.info("Reply: " + reply);
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

                        // TODO: set up user passable sending timeout!
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
        }
        else {
            System.out.println(Thread.currentThread().getName() + " Failed: Message Service or Message Generator is invalid!");
        }
    }
}
*/
/*
@Component
@Scope("prototype")
public class Producer extends Thread {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private volatile DSessionData sessionData;
    private volatile boolean fRunning = false;

    public Producer(DSessionData sessionData) {
        this.sessionData = sessionData;
        fRunning = false;
    }

    public void Terminate() {
        this.fRunning = false;
    }

    @Override
    public void run() {

        // start sending messages until thread is run down...
        MessageService messageService = sessionData.getMessageService();
        if (messageService == null) {
            // logita virhe ja tee jotakin...!!!!
            // TODO: what happens if this is taken place? Form is still opened...!

            return;
        }

        fRunning = true;

        // TODO: message types could be enumerated.

        MessageGenerator msgHandler = null;
*/
     /*   switch (sessionData.getStrMsgType()) {
            case "Integeri":
                msgHandler = new MessageGeneratorInteger();
                break;
            case "Decimali":
                msgHandler = new MessageGeneratorDecimal();
                break;
            case "Filei":
                msgHandler = new MessageGeneratorFile();
                break;
        }
*/
/*        if (msgHandler == null || !msgHandler.setUpMessaging(sessionData)) {
            //TODO: log some error stuff....
            return;
        }

        while (fRunning) {

            try {
                String strMessage = msgHandler.getNextMessage();
                if (!strMessage.isEmpty()) {
                    String reply = messageService.send(strMessage);
                    System.out.println(reply);
                    logger.info("Reply: " + reply);
                }
                else {
                    System.out.println("Empty message!!!!!!!");
                }
            } catch (Exception e) {
                System.out.println("Sending failed: " + e.getMessage().toString());

                logger.info("error in message sending...");
                try {
                    Thread.sleep(300);

                    // TODO: set up user passable sending timeout!
                }
                catch (Exception se) {

                }

                continue;   // There may be no listeners, so just continue sending...
            }

            try {
                Thread.sleep(500);
            }
            catch (Exception e) {

            }

        }

    }
*/
    /*
    @RequestMapping("/send/{message}")
    public String send(@PathVariable("message") String message) {
        try {
            String reply = messageService.send(message);
            System.out.println(reply);
            logger.info("Reply: " + reply);
        }
        catch (Exception e) {
            System.out.println(e.getMessage().toString());

        }
        return "/send";
    }
    */
//}
