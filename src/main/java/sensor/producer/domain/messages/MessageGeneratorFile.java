package sensor.producer.domain.messages;

import org.joda.time.DateTime;
import sensor.producer.data.Sensor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by mika on 3.6.2016.
 */
public class MessageGeneratorFile implements MessageGenerator {

    private String sensorId;
    private int threadId;
    private String strFile = null;
    private BufferedReader br = null;
    private int iFileStart;

    @Override
    public boolean setUpMessaging(Sensor sensor, Integer iThreadNbr) {
        if ( sensor == null || sensor.getMessageFile().isEmpty() ) {
            return false;
        }

        // TODO: get message file path from sessionData first....!!!!!
        this.sensorId = sensor.getSensorId();
        this.threadId = iThreadNbr;
        strFile = sensor.getMessageFile();

        try {
            br = SetupFileReader();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String getNextMessage() {
        if (br == null) {
            return null;
        }
        boolean fRead = true;
        StringBuffer strRet = new StringBuffer();
        DateTime eventTime = DateTime.now();

        while (fRead) {
            try {
                String strTemp;

                if ( (strTemp = br.readLine()) != null ) {
                    // At the end of the file... start again!
                    strRet.append(sensorId);
                    strRet.append(",");
                    strRet.append(eventTime.toString());
                    strRet.append(",");
                    strRet.append(strTemp);

                    fRead = false;
                }
                else {
                    // BufferedReader mark does not really work very well:
                    // It tends to read the whole file in the buffer and the
                    // reset point is only valid for the set buffer size...
                    // So lets just reopen the reader to reset it!

                    br.close();
                    br = SetupFileReader();
                }
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
                fRead = false;
                return null;
            }
        }

        System.out.println("Message to be sent (thread #" + threadId + "): " + strRet.toString());

        return strRet.toString();
    }

    @Override
    public boolean closeMessaging() {
        try {
            br.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }


    // =========================================================================
    private BufferedReader SetupFileReader() throws IOException {
        br = new BufferedReader(new FileReader(strFile));
        return br;
    }
}
