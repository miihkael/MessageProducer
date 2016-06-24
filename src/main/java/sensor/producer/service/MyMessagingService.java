package sensor.producer.service;

import sensor.producer.data.DSessionData;

/**
 * Created by mika on 14.6.2016.
 */
public interface MyMessagingService {
    public boolean startMessaging(DSessionData sessionData);
}
