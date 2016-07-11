package sensor.producer.domain.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import sensor.producer.data.DSessionData;
import sensor.producer.data.MessagePojo;
import sensor.producer.data.Sensor;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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
    private List<DSessionData.SENSORDATATYPE> sensDataTypes;

    public MessageGeneratorImpl() {
        sensDataTypes = new ArrayList<>();
    }

    @Override
    public Boolean setUpMessaging(Sensor sensor, List<DSessionData.SENSORDATATYPE> dataTypes, Integer iThreadNbr) {
        if (sensor == null) {
            return false;
        }

        this.sensorId = sensor.getSensorId();
        this.sensDataTypes = dataTypes;
        this.threadId = iThreadNbr;
        return true;
    }

    @Override
    public String getNextMessage() {

        // 1. Create new instance of MessagePojo for json creation.
        // 2. Create StringWriter to hold the json by...
        // 3. ... new ObjectMapper: fill json-string from MessagePojo.

        //MessagePojo mess = new MessagePojo(sensorId, sensDType);
        MessagePojo mess = new MessagePojo(sensorId, sensDataTypes);
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

        System.out.println("Data through toString: " + mess.toString());


        return sResult.toString();
    }

    @Override
    public Boolean closeMessaging() {
        // Nothing to do...
        return true;
    }
}
