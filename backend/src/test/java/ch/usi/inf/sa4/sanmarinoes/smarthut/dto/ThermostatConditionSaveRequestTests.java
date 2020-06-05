package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Thermostat;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ThermostatCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ThermostatConditionSaveRequest tests")
public class ThermostatConditionSaveRequestTests {

    private ThermostatConditionSaveRequest saveRequest;

    @BeforeEach
    private void createSaveRequest() {
        this.saveRequest = new ThermostatConditionSaveRequest();
    }

    @Test
    @DisplayName("test setDeviceId")
    public void testSetDeviceId() {
        this.saveRequest.setDeviceId(42L);
        assertEquals(42L, saveRequest.getDeviceId());
    }

    @Test
    @DisplayName("test setAutomationId")
    public void testSetAutomationId() {
        this.saveRequest.setAutomationId(42L);
        assertEquals(42L, saveRequest.getAutomationId());
    }

    @Test
    @DisplayName("test setOperator")
    public void testSetOperatorEqual() {
        saveRequest.setOperator(ThermostatCondition.Operator.EQUAL);
        assertEquals(ThermostatCondition.Operator.EQUAL, saveRequest.getOperator());
    }

    @Test
    @DisplayName("test setOperator")
    public void testSetOperatorNotEqual() {
        saveRequest.setOperator(ThermostatCondition.Operator.NOTEQUAL);
        assertEquals(ThermostatCondition.Operator.NOTEQUAL, saveRequest.getOperator());
    }

    @Test
    @DisplayName("test setMode")
    public void testSetMode() {
        saveRequest.setMode(Thermostat.Mode.HEATING);
        assertEquals(Thermostat.Mode.HEATING, saveRequest.getMode());
    }

    @Test
    @DisplayName("test getId")
    public void testGetId() {
        assertEquals(0L, saveRequest.getId());
    }
}
