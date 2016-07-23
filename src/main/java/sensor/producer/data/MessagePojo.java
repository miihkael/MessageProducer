package sensor.producer.data;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mika on 7.7.2016.
 *
 * Class for sensor message json building.
 *
 * Notice that the actual sensor data is stored as object(s), so that the main json parsing
 * is able to create that correctly.
 *
 * Instance(s) of this class is created by MessageGeneratorImpl, and called repeatedly by
 * Producer while it is sending sensor messages.
 *
 */
public class MessagePojo {
    private String apikey;
    private long timestamp;
    private List<DataPojo> data;

    public MessagePojo(String token, List<DSessionData.SENSORDATATYPE> datatypes ) {
        this.apikey = token;
        this.timestamp = (new Date()).getTime();
        this.data = new ArrayList<>();
        parseDataString(datatypes);
    }

    public String getApikey() {
        return apikey;
    }

    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<DataPojo> getData() {
        return data;
    }
    public void setData(List<DataPojo> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder sRet = new StringBuilder();
        sRet.append(
                "MessagePojo{" +
                "\"token\":\"" + apikey + "\"" +
                ",\"timestamp\":" + timestamp +
                ",\"data\":["
        );
        for (DataPojo currData : data) {
            sRet.append(currData.toString());

        }
        sRet.append("]}");

        return sRet.toString();

        // Example:
        // {"apikey": "adsfdsafdsf","timestamp":12345678901234,
        // "data": [{"datatype": "ACC","value": {"x": 0.4. "y": 0.3, "z": 0.3}}, {"datatype": "HUM", "value": 20}]}
    }

    // =======================================================000
    private void parseDataString(List<DSessionData.SENSORDATATYPE> datatypes) {
        // For most cases our sensors only have one data type present at once.
        // But we always store data into the data-array to cover multi type cases also.

        StringBuilder sRet = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.###");
        double lVal = 0;
        int iLow = 0;
        int iHigh =  0;
        boolean fStore = false;

        for (DSessionData.SENSORDATATYPE dType: datatypes ) {
            DataPojo dataPojo = new DataPojo(dType);
            fStore = false;

            switch (dType) {
                case ACC:
                case GYR:
                case MAG:
                    dataPojo.setValue(new SubXYZPojo());
                    fStore = true;
                    break;
                case ALT:
                    iLow = -14000;
                    iHigh = 50000;
                    lVal = (Math.random() * (iHigh - iLow)) + iLow;
                    df.applyPattern("#####.##");
                    df.setRoundingMode(RoundingMode.DOWN);
                    sRet.append(df.format(lVal));

                    dataPojo.setValue( new Double(Double.valueOf(sRet.toString())) );
                    fStore = true;
                    break;
                case BAR:
                    iLow = 920;
                    iHigh = 1080;
                    lVal = (Math.random() * (iHigh - iLow)) + iLow;
                    df.applyPattern("####");
                    df.setRoundingMode(RoundingMode.DOWN);
                    sRet.append(df.format(lVal));

                    dataPojo.setValue( new Integer(Integer.valueOf(sRet.toString())) );
                    fStore = true;
                    break;
                case FLAG:
                    lVal = Math.random();
                    if (lVal < 0.5) {
                        sRet.append("false");
                    }
                    else {
                        sRet.append("true");
                    }
                    dataPojo.setValue( new Boolean(Boolean.valueOf(sRet.toString())) );
                    fStore = true;
                    break;
                case GPS:
                    dataPojo.setValue( new SubGPSPojo() );
                    fStore = true;
                    break;
                case HUM:
                    iLow = 0;
                    iHigh = 100;
                    lVal = Math.random() * (iHigh - iLow);
                    df.applyPattern("###");
                    df.setRoundingMode(RoundingMode.DOWN);

                    sRet.append(df.format(lVal));
                    dataPojo.setValue( new Integer(Integer.valueOf(sRet.toString())) );
                    fStore = true;
                    break;
                case LUX:
                    iLow = 0;
                    iHigh = 100000;
                    lVal = Math.random() * (iHigh - iLow);
                    df.applyPattern("######.####");
                    df.setRoundingMode(RoundingMode.DOWN);

                    sRet.append(df.format(lVal));
                    dataPojo.setValue( new Double(Double.valueOf(sRet.toString())) );
                    fStore = true;
                    break;
                case TMP:
                    iLow = 15;
                    iHigh = 25;
                    lVal = (Math.random() * (iHigh - iLow)) + iLow;
                    df.applyPattern("###.##");
                    df.setRoundingMode(RoundingMode.DOWN);

                    sRet.append(df.format(lVal));
                    dataPojo.setValue( new Double(Double.valueOf(sRet.toString())) );
                    fStore = true;
                    break;
            }
            if (fStore) {
                data.add(dataPojo);
            }
        }
    }


    /**
     * Class for storing data part of the message.
     *
     * Data can be set of multiple values (e.g. ACC and GPS types) or single value of a
     * basic data type. In any case data is stored as Object.
     *
     */
    class DataPojo {
        private DSessionData.SENSORDATATYPE datatype;
        private Object value;

        public DataPojo(DSessionData.SENSORDATATYPE dtype) {
            this.datatype = dtype;
        }

        public DSessionData.SENSORDATATYPE getDatatype() {
            return datatype;
        }
        public void setDatatype(DSessionData.SENSORDATATYPE datatype) {
            this.datatype = datatype;
        }

        public Object getValue() {
            return value;
        }
        public void setValue(Object value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "{\"datatype\":\"" + datatype.toString() + "\"," +
                    "\"value\":" + value.toString() + "}";
        }
    }


    /**
     * Class for data having three axis.
     *
     */
    class SubXYZPojo {
        private Double X;
        private Double Y;
        private Double Z;
        private final double lHigh = 1.0;
        private final double lLow = -1.0;
        DecimalFormat df;

        public SubXYZPojo() {
            this.X = (Math.random() * (lHigh - lLow)) + lLow;
            this.Y = (Math.random() * (lHigh - lLow)) + lLow;
            this.Z = (Math.random() * (lHigh - lLow)) + lLow;

            df = new DecimalFormat("#.###");
            df.setRoundingMode(RoundingMode.DOWN);
        }

        public Double getX() {
            String sRet = df.format(X);
            return new Double(Double.valueOf(sRet.toString()));
        }

        public Double getY() {
            String sRet = df.format(Y);
            return new Double(Double.valueOf(sRet.toString()));
        }

        public Double getZ() {
            String sRet = df.format(Z);
            return new Double(Double.valueOf(sRet.toString()));
        }

        @Override
        public String toString() {
            return "{\"X\":" + df.format(X) + "," +
                    "\"Y\":" + df.format(Y) + "," +
                    "\"Z\":" + df.format(Z) + "}";
        }
    }


    /**
     * Class for GPS-data having latitude and longitude members.
     *
     */
    class SubGPSPojo {
        private double LAT;
        private double LON;
        private final double lLatLow = -90.0;
        private final double lLatHigh = 90.0;
        private final double lLongLow = -180.0;
        private final double lLongHigh = 180.0;
        private DecimalFormat df;

        public SubGPSPojo() {
            LAT = (Math.random() * (lLatHigh - lLatLow)) + lLatLow;
            LON = (Math.random() * (lLongHigh - lLongLow)) + lLongLow;
            df = new DecimalFormat("###.####");
            df.setRoundingMode(RoundingMode.DOWN);
        }

        public double getLAT() {
            String sRet = df.format(LAT);
            return new Double(Double.valueOf(sRet.toString()));
        }

        public double getLON() {
            String sRet = df.format(LON);
            return new Double(Double.valueOf(sRet.toString()));
        }

        @Override
        public String toString() {
            return "{\"LAT\":" + df.format(LAT) + "," +
                    "\"LON\":" + df.format(LON) + "}";
        }
    }
}
