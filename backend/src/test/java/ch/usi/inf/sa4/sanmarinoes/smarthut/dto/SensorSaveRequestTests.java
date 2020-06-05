package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Sensor;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SensorSaveRequest tests")
public class SensorSaveRequestTests {

    private SensorSaveRequest sensor;

    @BeforeEach
    public void createSensorSaveRequest() {
        this.sensor = new SensorSaveRequest();
    }

    @Test
    @DisplayName("test setRoomId")
    public void testSetRoomId() {
        sensor.setRoomId(42L);
        assertEquals(42L, sensor.getRoomId());
    }

    @Test
    @DisplayName("test constructor")
    public void testConstructorNotEmpty() {
        SensorSaveRequest newSaveRequest =
                new SensorSaveRequest(Sensor.SensorType.HUMIDITY, new BigDecimal(12), 12L, "name");
        assertNotNull(newSaveRequest.getSensor());
        assertNotNull(newSaveRequest.getName());
        assertNotNull(newSaveRequest.getRoomId());
        assertNotNull(newSaveRequest.getValue());
    }

    @Test
    @DisplayName("test setName")
    public void testSetName() {
        sensor.setName("Giovanni");
        assertEquals("Giovanni", sensor.getName());
    }

    @Test
    @DisplayName("test setValue")
    public void testSetValue() {
        sensor.setValue(new BigDecimal(42));
        assertEquals(new BigDecimal(42), sensor.getValue());
    }

    @Test
    @DisplayName("test set to TEMPERATURE")
    public void testSetToTemperature() {
        sensor.setSensor(Sensor.SensorType.TEMPERATURE);
        assertEquals(Sensor.SensorType.TEMPERATURE, sensor.getSensor());
    }

    @Test
    @DisplayName("test set to HUMIDITY")
    public void testSetToHumidity() {
        sensor.setSensor(Sensor.SensorType.HUMIDITY);
        assertEquals(Sensor.SensorType.HUMIDITY, sensor.getSensor());
    }

    @Test
    @DisplayName("test set to LIGHT")
    public void testSetToLight() {
        sensor.setSensor(Sensor.SensorType.LIGHT);
        assertEquals(Sensor.SensorType.LIGHT, sensor.getSensor());
    }

    @Test
    @DisplayName("test SensorType")
    public void testSetSensorType() {
        assertEquals("TEMPERATURE", Sensor.SensorType.TEMPERATURE.name());
        assertEquals("HUMIDITY", Sensor.SensorType.HUMIDITY.name());
        assertEquals("LIGHT", Sensor.SensorType.LIGHT.name());
    }
}
