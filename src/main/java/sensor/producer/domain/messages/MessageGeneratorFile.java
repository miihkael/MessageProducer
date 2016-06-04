package sensor.producer.domain.messages;

import sensor.producer.data.DSessionData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by mika on 3.6.2016.
 */
public class MessageGeneratorFile implements MessageGenerator {

    private String sensorId;
    private String strFile = null;
    private BufferedReader br = null;
    private int iFileStart;

    @Override
    public boolean setUpMessaging(DSessionData sessionData) {
        if ( sessionData == null || sessionData.getStrMsgFile().isEmpty() ) {
            return false;
        }

        sensorId = sessionData.getStrSensorId();
        strFile = sessionData.getStrMsgFile();

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

        while (fRead) {
            try {
                String strTemp;

                if ( (strTemp = br.readLine()) != null ) {
                    // At the end of the file... start again!
                    strRet.append(sensorId);
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

        System.out.println("Message to be sent: " + strRet.toString());

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
