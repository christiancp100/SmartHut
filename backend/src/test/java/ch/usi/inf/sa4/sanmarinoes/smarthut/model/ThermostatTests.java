package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Thermostat;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Thermostat tests")
public class ThermostatTests {
    private Thermostat thermostat;

    @BeforeEach
    public void createThermostat() {
        this.thermostat = new Thermostat();
    }

    @Test
    @DisplayName("test isOn()")
    public void inOnTest() {
        assertFalse(thermostat.isOn());
    }

    @Test
    @DisplayName("test setOn(true) ")
    public void setOnTestTrue() {
        thermostat.setOn(true);
        assertTrue(thermostat.isOn());
    }

    @Test
    @DisplayName("test setOn(false) ")
    public void setOnTestFalse() {
        thermostat.setOn(false);
        assertFalse(thermostat.isOn());
    }

    @Test
    @DisplayName("test compute measured is null")
    public void computeMeasureNull() {
        this.thermostat.setMeasuredTemperature(null);
        thermostat.setOn(true);
        assertEquals(Thermostat.Mode.IDLE, thermostat.getMode());
    }

    @Test
    @DisplayName("test compute |measured-target|<0.25")
    public void computeMeasureIdle() {
        this.thermostat.setMeasuredTemperature(new BigDecimal(1));
        this.thermostat.setTargetTemperature(new BigDecimal(0.9));
        thermostat.setOn(true);
        assertEquals(Thermostat.Mode.IDLE, thermostat.getMode());
    }

    @Test
    @DisplayName("test compute heating")
    public void computeMeasureHeating() {
        this.thermostat.setMeasuredTemperature(new BigDecimal(1));
        this.thermostat.setTargetTemperature(new BigDecimal(2));
        thermostat.setOn(true);
        assertEquals(Thermostat.Mode.HEATING, thermostat.getMode());
    }

    @Test
    @DisplayName("test compute cooling")
    public void computeMeasureCooling() {
        this.thermostat.setMeasuredTemperature(new BigDecimal(10));
        this.thermostat.setTargetTemperature(new BigDecimal(5));
        thermostat.setOn(true);
        assertEquals(Thermostat.Mode.COOLING, thermostat.getMode());
    }

    @Test
    @DisplayName("test external sensor")
    public void testExternalSensor() {
        thermostat.setUseExternalSensors(true);
        assertTrue(thermostat.isUseExternalSensors());
    }

    @Test
    @DisplayName("test internal sensor temperature")
    public void testInternalSensorTemperature() {
        thermostat.setInternalSensorTemperature(new BigDecimal(42));
        assertEquals(new BigDecimal(42), thermostat.getInternalSensorTemperature());
    }

    @Test
    @DisplayName("test triggerState")
    public void testTriggerState() {
        assertFalse(thermostat.readTriggerState());
    }

    // for obvious reasons I am not gonna check all the possible combinations of toString()
    @Test
    @DisplayName("test to string")
    public void testToString() {
        thermostat.setMeasuredTemperature(new BigDecimal(1));
        thermostat.setTargetTemperature(new BigDecimal(1));
        assertEquals(
                "Thermostat{targetTemperature=1, internalSensorTemperature=17.0, mode=OFF, measuredTemperature=1, useExternalSensors=false}",
                thermostat.toString());
    }
}
