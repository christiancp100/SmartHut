package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ThermostatSaveRequest tests")
public class ThermostatSaveRequestTests {
    private ThermostatSaveRequest saveRequest;

    @BeforeEach
    private void createSaveRequest() {
        saveRequest = new ThermostatSaveRequest();
    }

    @Test
    @DisplayName("test setRoomId")
    public void testSetRoomId() {
        saveRequest.setRoomId(42l);
        assertEquals(42l, saveRequest.getRoomId());
    }

    @Test
    @DisplayName("test setName")
    public void testSetName() {
        saveRequest.setName("Giovanni");
        assertEquals("Giovanni", saveRequest.getName());
    }

    @Test
    @DisplayName("test isOn()")
    public void inOnTest() {
        assertFalse(saveRequest.isTurnOn());
    }

    @Test
    @DisplayName("test setOn(true) ")
    public void setOnTestTrue() {
        saveRequest.setTurnOn(true);
        assertTrue(saveRequest.isTurnOn());
    }

    @Test
    @DisplayName("test setOn(false) ")
    public void setOnTestFalse() {
        saveRequest.setTurnOn(false);
        assertFalse(saveRequest.isTurnOn());
    }

    @Test
    @DisplayName("test setId")
    public void testSetId() {
        saveRequest.setId(17l);
        assertEquals(17l, saveRequest.getId());
    }

    @Test
    @DisplayName("test setExternalSensor true")
    public void testExternalSensorTrue() {
        saveRequest.setUseExternalSensors(true);
        assertTrue(saveRequest.isUseExternalSensors());
    }

    @Test
    @DisplayName("test setExternalSensor false")
    public void testExternalSensorFalse() {
        saveRequest.setUseExternalSensors(false);
        assertFalse(saveRequest.isUseExternalSensors());
    }

    @Test
    @DisplayName("test targetTemperature")
    public void testTargetTemperature() {
        saveRequest.setTargetTemperature(new BigDecimal(23));
        assertEquals(new BigDecimal(23), saveRequest.getTargetTemperature());
    }
}
