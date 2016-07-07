package sensor.producer.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mika on 17.6.2016.
 *
 * This class is used for displaying sensors in .jsp forms.
 */
public class SensorForm {
    private List<Sensor> sensorsInForm;

    // THE DEFAULT CONSTRUCTOR MUST BE IMPLEMENTED HERE !!!!
    // OTHERWISE FORM POST CANNOT CREATE THIS CLASS INSTANCE !!!!
    public SensorForm() {
        sensorsInForm = new ArrayList<Sensor>();
    }

    public List<Sensor> getSensorsInForm() {
        return sensorsInForm;
    }

    public void setSensorsInForm(List<Sensor> sensorsInForm) {
        this.sensorsInForm = sensorsInForm;
    }
}
