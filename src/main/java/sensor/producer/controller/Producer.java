package sensor.producer.controller;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sensor.producer.data.DSessionData;
import sensor.producer.domain.MessageService;
import sensor.producer.domain.messages.MessageGenerator;
import sensor.producer.domain.messages.MessageGeneratorDecimal;
import sensor.producer.domain.messages.MessageGeneratorFile;
import sensor.producer.domain.messages.MessageGeneratorInteger;

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

        switch (sessionData.getStrMsgType()) {
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

        if (msgHandler == null || !msgHandler.setUpMessaging(sessionData)) {
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
}
