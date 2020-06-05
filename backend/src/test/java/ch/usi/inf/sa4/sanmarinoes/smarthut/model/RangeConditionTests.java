package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.DimmableLight;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Operator;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.RangeCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RAnge Condition Tests")
public class RangeConditionTests {

    private RangeCondition rangeCondition;

    @BeforeEach
    public void creteRangeCondition() {
        this.rangeCondition = new RangeCondition();
    }

    @Test
    @DisplayName("set and get operator")
    public void setAndGetOperator() {
        rangeCondition.setOperator(Operator.EQUAL);

        assertEquals(Operator.EQUAL, rangeCondition.getOperator());
    }

    @Test
    @DisplayName("set and get range")
    public void setAndGetRange() {
        rangeCondition.setRange(20.5);

        assertEquals(20.5, rangeCondition.getRange());
    }

    @Test
    @DisplayName("triggered")
    public void triggered() {
        DimmableLight d = new DimmableLight();
        d.setIntensity(40);
        rangeCondition.setDevice(d);
        rangeCondition.setRange(45D);

        rangeCondition.setOperator(Operator.EQUAL);
        assertFalse(rangeCondition.triggered());

        rangeCondition.setOperator(Operator.LESS);
        assertTrue(rangeCondition.triggered());

        rangeCondition.setOperator(Operator.GREATER);
        assertFalse(rangeCondition.triggered());

        rangeCondition.setOperator(Operator.LESS_EQUAL);
        assertTrue(rangeCondition.triggered());

        rangeCondition.setOperator(Operator.GREATER_EQUAL);
        assertFalse(rangeCondition.triggered());
    }
}
