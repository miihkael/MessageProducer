package sensor.producer.domain.messages;

import sensor.producer.data.DSessionData;
import sensor.producer.data.Sensor;

import java.util.List;

/**
 * Created by mika on 3.6.2016.
 */
public interface MessageGenerator {

    public Boolean setUpMessaging(Sensor sensor, List<DSessionData.SENSORDATATYPE> dataTypes, Integer iThreadNbr);
    public String getNextMessage();
    public Boolean closeMessaging();

}
