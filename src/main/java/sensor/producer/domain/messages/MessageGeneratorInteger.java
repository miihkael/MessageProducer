package sensor.producer.domain.messages;

import sensor.producer.data.DSessionData;

/**
 * Created by mika on 3.6.2016.
 */
public class MessageGeneratorInteger implements MessageGenerator {

    private String sensorId;
    private final int iLow = -50;
    private final int iHigh = 50;

    @Override
    public boolean setUpMessaging(DSessionData sessionData) {
        if (sessionData == null) {
            return false;
        }

        sensorId = sessionData.getStrSensorId();
        return true;
    }

    @Override
    public String getNextMessage() {
        int iRand = (int)(Math.random() * (iHigh - iLow)) + iLow ;

        String strRes = sensorId + "," + iRand;

        System.out.println("Message to be sent: " + strRes);

        return strRes;
    }

    @Override
    public boolean closeMessaging() {
        // Nothing to do...
        return true;
    }
}
