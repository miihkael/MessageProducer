package sensor.producer.domain.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import sensor.producer.data.DSessionData;
import sensor.producer.data.MessagePojo;
import sensor.producer.data.Sensor;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by mika on 3.6.2016.
 *
 * This class acts as message generator for all sensor data types.
 * It creates a MessagePojo object that is used for createting message json.
 * Any one such MessagePojo object represents one sensor data message to be sent.
 *
 * It is important to check the message data type when parsing json back to
 * message data, since the actual data can be present on json string itself
 * (e.g. for ACC, GYR and MAG types of sensor data): Use HasMap to parse the
 * data from string returning by getData-method in these cases.
 *
 * Class instance is used by message producer (see. Producer) to generate
 * new dummy messages for the sensor accoring the the sensor type.
 * Producer calls repeatedly getNextMessage() for this task.
 *
 */
public class MessageGeneratorImpl implements MessageGenerator {

    private String sensorId;
    private int threadId;
    private DSessionData.SENSORDATATYPE sensDType;

    public MessageGeneratorImpl(DSessionData.SENSORDATATYPE sType) {
        sensDType = sType;
    }

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

        MessagePojo mess = new MessagePojo(sensorId, sensDType);
        StringWriter sResult = new StringWriter();

        if (mess != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                mapper.writeValue(sResult, mess);


                //System.out.println("Mapper toString: " + mapper.writeValueAsString(mess));
            }
            catch (IOException ioe) {
                System.out.println("MessagePojo to ObjectMapper failed: " + ioe.getMessage());
            }
        }


        System.out.println("Message to be sent (thread #" + threadId + "): " + sResult.toString());
        return sResult.toString();
    }

    @Override
    public boolean closeMessaging() {
        // Nothing to do...
        return true;
    }
}
