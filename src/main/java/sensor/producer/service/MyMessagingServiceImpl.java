package sensor.producer.service;

import org.springframework.stereotype.Service;
import sensor.producer.controller.Producer;
import sensor.producer.data.DSessionData;
import sensor.producer.data.Sensor;
import sensor.producer.domain.messages.*;

import static sensor.producer.data.DSessionData.maxMessagingCount;

/**
 * Created by mika on 14.6.2016.
 */
@Service
public class MyMessagingServiceImpl implements MyMessagingService {

    @Override
    public Integer startMessaging(DSessionData sessionData) {
        // Start message sending for selected sensors (sensors in activated list of sessionData).
        // Notice that maximum of 20 sensors can run at once (implemented restriction).

        int iRet = 0;
        int iThreadId = 1;
        MessageGenerator newMG = null;
        boolean fCont = false;

        /*for ( Sensor sensor : sessionData.getSensors() ) {
            if ( sessionData.getActiveSensCount() < maxMessagingCount ) {
                if ( sensor.isSelected() ) {
                    fCont = true;
                    switch (sensor.getSensorType()) {
                        case INTEGER:
                            newMG = new MessageGeneratorInteger();
                            newMG.setUpMessaging(sensor, iThreadId);
                            fCont = true;
                            break;
                        case DECIMAL:
                            newMG = new MessageGeneratorDecimal();
                            newMG.setUpMessaging(sensor, iThreadId);
                            break;
                        case FILE:
                            newMG = new MessageGeneratorFile();
                            newMG.setUpMessaging(sensor, iThreadId);
                            fCont = true;
                            break;
                        case BOOLEAN:
                            System.out.println("MyMessagingServiceImpl::startMessaging(): BOOLEAN-type not implemented!");
                            fCont = false;
                            break;
                        default:
                            fCont = false;
                            break;
                    }

                    if (fCont) {
                        try {
                            Producer sender = new Producer(sessionData.getMessageService(),
                                                            newMG,
                                                            sensor.getTimeOut(),
                                                            iThreadId);
                            iThreadId++;
                            sensor.setThread(sender);
                            sessionData.addActiveSensor(sensor);
                            sensor.setStrStatus("Sending...");

                            sender.start();
                        } catch (Exception e) {
                            System.out.println("MyMessagingServiceImpl::startMessaging(): Failed to create messaging thread " +
                                    (iThreadId+1) + " " + e.getMessage());
                            sensor.setStrStatus("Not sending...");
                        }
                    }
                    fCont = true;
                }
            }
        }*/

        for ( Sensor sensor : sessionData.getSensors() ) {
            if ( sessionData.getActiveSensCount() < maxMessagingCount ) {
                if ( sensor.isSelected() ) {

                    DSessionData.SENSORDATATYPE sType = sensor.getSensorDataType();

                    newMG = new MessageGeneratorImpl(sType);

                    newMG.setUpMessaging(sensor, iThreadId);
                    try {
                        Producer sender = new Producer(sessionData.getMessageService(),
                                newMG,
                                sensor.getTimeOut(),
                                iThreadId);
                        iThreadId++;
                        sensor.setThread(sender);
                        sessionData.addActiveSensor(sensor);    // Add to active sensors -list.
                        sensor.setStrStatus("Sending...");

                        sender.start();
                    } catch (Exception e) {
                        System.out.println("MyMessagingServiceImpl::startMessaging(): Failed to create messaging thread " +
                                (iThreadId+1) + " " + e.getMessage());
                        sensor.setStrStatus("Not sending...");
                    }

                }
            }
        }

        return iRet;
    }


    @Override
    public boolean stopMessaging(DSessionData sessionData) {
        boolean fRet = false;

        if (sessionData != null) {
            sessionData.deleteActiveSensors();
            fRet = true;
        }

        return fRet;
    }
}

