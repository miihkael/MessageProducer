package sensor.producer.domain.messages;

import org.joda.time.DateTime;
import sensor.producer.data.Sensor;

/**
 * Created by mika on 3.6.2016.
 */

// OBSOLETE

public class MessageGeneratorInteger implements MessageGenerator {

    private String sensorId;
    private int threadId;
    private final int iLow = -50;
    private final int iHigh = 50;

    @Override
    public boolean setUpMessaging(Sensor sensor, Integer iThreadNbr) {
        if (sensor == null) {
            return false;
        }

        this.sensorId = sensor.getSensorId();
        this.threadId = iThreadNbr;
        return true;
    }

    @Override
    public String getNextMessage() {
        int iRand = (int)(Math.random() * (iHigh - iLow)) + iLow;
        DateTime eventTime = DateTime.now();

        String strRes = sensorId + "," + eventTime.toString() + "," + iRand;

        System.out.println("Message to be sent (thread #" + threadId + "): " + strRes);

        return strRes;
    }

    @Override
    public boolean closeMessaging() {
        // Nothing to do...
        return true;
    }
}
