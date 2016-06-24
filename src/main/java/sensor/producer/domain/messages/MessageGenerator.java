package sensor.producer.domain.messages;

import sensor.producer.data.Sensor;

/**
 * Created by mika on 3.6.2016.
 */
public interface MessageGenerator {

    public boolean setUpMessaging(Sensor sensor, Integer iThreadNbr);
    public String getNextMessage();
    public boolean closeMessaging();

}
