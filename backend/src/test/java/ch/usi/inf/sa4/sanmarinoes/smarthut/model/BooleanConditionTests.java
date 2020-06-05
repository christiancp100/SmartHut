package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.BooleanCondition;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Switch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("BooleanCondition tests")
public class BooleanConditionTests {
    private BooleanCondition condition;

    @BeforeEach
    private void createCondition() {
        condition = new BooleanCondition();
    }

    @Test
    @DisplayName("test setOnFalse")
    public void testSetOnFalse() {
        condition.setOn(false);
        assertFalse(condition.isOn());
    }

    @Test
    @DisplayName("test setOnTrue")
    public void testSetOnTrue() {
        condition.setOn(true);
        assertTrue(condition.isOn());
    }

    @Test
    @DisplayName("test triggeredTrue")
    public void testTriggeredTrue() {
        Switch a = new Switch();
        a.setOn(true);
        condition.setOn(true);
        condition.setDevice(a);
        assertTrue(condition.triggered());
        condition.setOn(false);
        assertFalse(condition.triggered());
    }

    @Test
    @DisplayName("test triggeredFalse")
    public void testTriggeredFalse() {
        Switch a = new Switch();
        a.setOn(false);
        condition.setOn(false);
        condition.setDevice(a);
        assertTrue(condition.triggered());
        condition.setOn(true);
        assertFalse(condition.triggered());
    }
}
