package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Sensor;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Sensor.SensorType;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Sensor Tests")
public class SensorTests {

    Sensor sensor;

    @BeforeEach
    public void createSensor() {
        this.sensor = new Sensor();
    }

    @Test
    @DisplayName("get and set sensor")
    public void getAndSetSensor() {
        sensor.setSensor(SensorType.LIGHT);

        assertEquals(SensorType.LIGHT, sensor.getSensor());
    }

    @Test
    @DisplayName("get and set value")
    public void getAndSetValue() {
        sensor.setValue(new BigDecimal(40));

        assertEquals(new BigDecimal(40), sensor.getValue());
    }

    @Test
    @DisplayName("to String")
    public void toStringTest() {
        sensor.setValue(new BigDecimal(40));
        sensor.setSensor(SensorType.LIGHT);

        assertEquals("Sensor{value=40, sensor=LIGHT}", sensor.toString());
    }
}
