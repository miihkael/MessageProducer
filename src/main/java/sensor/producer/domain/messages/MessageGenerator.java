package sensor.producer.domain.messages;

import sensor.producer.data.DSessionData;

/**
 * Created by mika on 3.6.2016.
 */
public interface MessageGenerator {

    public boolean setUpMessaging(DSessionData sessionData);
    public String getNextMessage();
    public boolean closeMessaging();

}
