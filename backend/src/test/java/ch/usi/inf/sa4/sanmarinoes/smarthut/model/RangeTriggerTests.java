package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.DimmableLight;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Operator;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.RangeTrigger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Range Trigger Tests")
public class RangeTriggerTests {

    private RangeTrigger rangeTrigger;

    @BeforeEach
    public void createRangeTrigger() {
        this.rangeTrigger = new RangeTrigger();
    }

    @Test
    @DisplayName("set and get operator")
    public void setAndGetOperator() {
        rangeTrigger.setOperator(Operator.EQUAL);

        assertEquals(Operator.EQUAL, rangeTrigger.getOperator());
    }

    @Test
    @DisplayName("set and get range")
    public void setAndGetRange() {
        rangeTrigger.setRange(20.5);

        assertEquals(20.5, rangeTrigger.getRange());
    }

    @Test
    @DisplayName("triggered")
    public void triggered() {
        DimmableLight d = new DimmableLight();
        d.setIntensity(40);
        rangeTrigger.setDevice(d);
        rangeTrigger.setRange(45D);

        rangeTrigger.setOperator(Operator.EQUAL);
        assertFalse(rangeTrigger.triggered());

        rangeTrigger.setOperator(Operator.LESS);
        assertTrue(rangeTrigger.triggered());

        rangeTrigger.setOperator(Operator.GREATER);
        assertFalse(rangeTrigger.triggered());

        rangeTrigger.setOperator(Operator.LESS_EQUAL);
        assertTrue(rangeTrigger.triggered());

        rangeTrigger.setOperator(Operator.GREATER_EQUAL);
        assertFalse(rangeTrigger.triggered());
    }
}
