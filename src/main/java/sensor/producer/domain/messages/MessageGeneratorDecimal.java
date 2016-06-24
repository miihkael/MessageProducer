package sensor.producer.domain.messages;

import org.joda.time.DateTime;
import sensor.producer.data.Sensor;

/**
 * Created by mika on 3.6.2016.
 */
public class MessageGeneratorDecimal implements MessageGenerator {
    private String sensorId;
    private int threadId;
    private final double lLow = 0.0;
    private final double lHigh = 1.0;

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
        double lRand = (Math.random() * (lHigh - lLow));
        DateTime eventTime = DateTime.now();

        String strRes = sensorId + "," + eventTime.toString() + "," + lRand;

        System.out.println("Message to be sent (thread #" + threadId + "): " + strRes);

        return strRes;
    }

    @Override
    public boolean closeMessaging() {
        // Nothing to do...
        return true;
    }

}
