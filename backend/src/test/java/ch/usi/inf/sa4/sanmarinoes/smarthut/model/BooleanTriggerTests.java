package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.BooleanTrigger;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Switch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("booleantrigger test")
public class BooleanTriggerTests {
    private BooleanTrigger trigger;

    @BeforeEach
    private void createTrigger() {
        trigger = new BooleanTrigger();
    }

    @Test
    @DisplayName("test setOnFalse")
    public void testSetOnFalse() {
        trigger.setOn(false);
        assertFalse(trigger.isOn());
    }

    @Test
    @DisplayName("test setOnTrue")
    public void testSetOnTrue() {
        trigger.setOn(true);
        assertTrue(trigger.isOn());
    }

    @Test
    @DisplayName("test triggeredTrue")
    public void testTriggeredTrue() {
        Switch a = new Switch();
        a.setOn(true);
        trigger.setOn(true);
        trigger.setDevice(a);
        assertTrue(trigger.triggered());
        trigger.setOn(false);
        assertFalse(trigger.triggered());
    }

    @Test
    @DisplayName("test triggeredFalse")
    public void testTriggeredFalse() {
        Switch a = new Switch();
        a.setOn(false);
        trigger.setOn(false);
        trigger.setDevice(a);
        assertTrue(trigger.triggered());
        trigger.setOn(true);
        assertFalse(trigger.triggered());
    }
}
