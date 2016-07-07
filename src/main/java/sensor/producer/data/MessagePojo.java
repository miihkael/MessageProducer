package sensor.producer.data;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by mika on 7.7.2016.
 *
 * Class for sensor message json building.
 *
 * Notice that the actual sensor data is stored as object, so that the main json parsing
 * is able to create that correctly.
 *
 * Instance(s) of this class is created by MessageGeneratorImpl, and called repeatedly by
 * Producer while it is sending sensor messages.
 *
 */
public class MessagePojo {
    private String id;
    private long timestamp;
    private DSessionData.SENSORDATATYPE datatype;
    private Object data;

    // Sub-objects:
    private SubXYZPojo subXYZPojo;

    public MessagePojo(String id, DSessionData.SENSORDATATYPE datatype ) {
        this.id = id;
        this.datatype = datatype;

        this.timestamp = (new Date()).getTime();
        parseDataString();
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public DSessionData.SENSORDATATYPE getDatatype() {
        return datatype;
    }
    public void setDatatype(DSessionData.SENSORDATATYPE datatype) {
        this.datatype = datatype;
    }

    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MessagePojo{" +
                "\"id\":\"" + id + "\"" +
                ",\"timestamp\":" + timestamp +
                ",\"datatype\":\"" + datatype.toString().toLowerCase() + "\"" +
                ",\"data\":" + data +
                '}';
    }

    // =======================================================000
    private void parseDataString() {


        StringBuilder sRet = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.###");
        double lVal = 0;
        int iLow = 0;
        int iHigh =  0;

        switch (datatype) {
            case ACC:
            case GYR:
            case MAG:
                this.data = (SubXYZPojo) new SubXYZPojo();
                break;
            case BAR:
                iLow = 920;
                iHigh = 1080;
                lVal = (Math.random() * (iHigh - iLow)) + iLow;
                df.applyPattern("####");
                df.setRoundingMode(RoundingMode.DOWN);
                sRet.append(df.format(lVal));

                this.data = new Integer(Integer.valueOf(sRet.toString()));
                break;
            case FLAG:
                lVal = Math.random();
                if (lVal < 0.5) {
                    sRet.append("false");
                }
                else {
                    sRet.append("true");
                }
                this.data = new Boolean(Boolean.valueOf(sRet.toString()));
                break;
            case GPS:
                this.data = (SubGPSPojo) new SubGPSPojo();
                break;
            case HUM:
                iLow = 0;
                iHigh = 100;
                lVal = Math.random() * (iHigh - iLow);
                df.applyPattern("###");
                df.setRoundingMode(RoundingMode.DOWN);

                sRet.append(df.format(lVal));
                this.data = new Integer(Integer.valueOf(sRet.toString()));
                break;
            case LUX:
                iLow = 0;
                iHigh = 100000;
                lVal = Math.random() * (iHigh - iLow);
                df.applyPattern("######.####");
                df.setRoundingMode(RoundingMode.DOWN);

                sRet.append(df.format(lVal));
                this.data = new Double(Double.valueOf(sRet.toString()));
                break;
            case TMP:
                iLow = -60;
                iHigh = 180;
                lVal = (Math.random() * (iHigh - iLow)) + iLow;
                df.applyPattern("###.##");
                df.setRoundingMode(RoundingMode.DOWN);

                sRet.append(df.format(lVal));
                this.data = new Double(Double.valueOf(sRet.toString()));
                break;
            default:
                this.data = null;
                break;
        }
    }


    // Classes for subdata items:
    class SubXYZPojo {
        private Double x;
        private Double y;
        private Double z;
        private final double lHigh = 1.0;
        private final double lLow = -1.0;
        DecimalFormat df;

        public SubXYZPojo() {
            this.x = (Math.random() * (lHigh - lLow)) + lLow;
            this.y = (Math.random() * (lHigh - lLow)) + lLow;
            this.z = (Math.random() * (lHigh - lLow)) + lLow;

            df = new DecimalFormat("#.###");
            df.setRoundingMode(RoundingMode.DOWN);
        }

        public Double getX() {
            String sRet = df.format(x);
            return new Double(Double.valueOf(sRet.toString()));
        }

        public Double getY() {
            String sRet = df.format(y);
            return new Double(Double.valueOf(sRet.toString()));
        }

        public Double getZ() {
            String sRet = df.format(z);
            return new Double(Double.valueOf(sRet.toString()));
        }
    }



    class SubGPSPojo {
        private double lat;
        private double lon;
        private final double lLatLow = -90.0;
        private final double lLatHigh = 90.0;
        private final double lLongLow = -180.0;
        private final double lLongHigh = 180.0;
        private DecimalFormat df;

        public SubGPSPojo() {
            lat = (Math.random() * (lLatHigh - lLatLow)) + lLatLow;
            lon = (Math.random() * (lLongHigh - lLongLow)) + lLongLow;
            df = new DecimalFormat("###.####");
            df.setRoundingMode(RoundingMode.DOWN);
        }

        public double getLat() {
            String sRet = df.format(lat);
            return new Double(Double.valueOf(sRet.toString()));
        }

        public double getLon() {
            String sRet = df.format(lon);
            return new Double(Double.valueOf(sRet.toString()));
        }
    }
}
