package sensor.producer.service;

import sensor.producer.data.DSessionData;

/**
 * Created by mika on 14.6.2016.
 */
public interface MyMessagingService {
    // Starts messaging sending for all active sensors.
    // Returns number of started messaging threads.
    public Integer startMessaging(DSessionData sessionData);

    // Stops message sending for all active sensors.
    public boolean stopMessaging(DSessionData sessionData);
}
