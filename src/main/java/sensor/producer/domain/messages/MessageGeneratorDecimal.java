package sensor.producer.domain.messages;

import sensor.producer.data.DSessionData;

/**
 * Created by mika on 3.6.2016.
 */
public class MessageGeneratorDecimal implements MessageGenerator {
    private String sensorId;
    private final double lLow = 0.0;
    private final double lHigh = 1.0;

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
        double lRand = (Math.random() * (lHigh - lLow))  ;

        String strRes = sensorId + "," + lRand;

        System.out.println("Message to be sent: " + strRes);

        return strRes;
    }

    @Override
    public boolean closeMessaging() {
        // Nothing to do...
        return true;
    }

}
