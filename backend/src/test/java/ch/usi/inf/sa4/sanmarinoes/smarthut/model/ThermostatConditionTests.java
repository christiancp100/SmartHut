package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Thermostat;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Thermostat.Mode;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ThermostatCondition;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ThermostatCondition.Operator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ThermostatCondition Tests")
public class ThermostatConditionTests {

    private ThermostatCondition thermostatCondition;

    @BeforeEach
    public void createThermostatCondtion() {
        this.thermostatCondition = new ThermostatCondition();
    }

    @Test
    @DisplayName("get and set mode")
    public void getAndSetMode() {
        thermostatCondition.setMode(Thermostat.Mode.IDLE);

        assertEquals(Thermostat.Mode.IDLE, thermostatCondition.getMode());
    }

    @Test
    @DisplayName("get and set operator")
    public void getAndSeOperator() {
        thermostatCondition.setOperator(Operator.EQUAL);

        assertEquals(Operator.EQUAL, thermostatCondition.getOperator());
    }

    @Test
    @DisplayName("get and set operator")
    public void triggered() {
        thermostatCondition.setMode(Thermostat.Mode.IDLE);
        thermostatCondition.setOperator(Operator.EQUAL);
        Thermostat t = new Thermostat();
        t.setMode(Mode.IDLE);
        thermostatCondition.setDevice(t);

        assertTrue(thermostatCondition.triggered());

        thermostatCondition.setOperator(Operator.NOTEQUAL);
        assertFalse(thermostatCondition.triggered());
    }
}
