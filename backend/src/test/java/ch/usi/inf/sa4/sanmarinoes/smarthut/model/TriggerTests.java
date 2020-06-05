package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Automation;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.BooleanTrigger;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.RegularLight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Trigger Tests")
public class TriggerTests {

    private BooleanTrigger booleanTrigger;

    @BeforeEach
    public void createBooleanTrigger() {
        booleanTrigger = new BooleanTrigger();
    }

    @Test
    @DisplayName("get Kind")
    public void getKind() {
        assertEquals("booleanTrigger", booleanTrigger.getKind());
    }

    @Test
    @DisplayName("get and set id")
    public void getAndSetId() {
        booleanTrigger.setId(20);
        assertEquals(20, booleanTrigger.getId());
    }

    @Test
    @DisplayName("get and set device")
    public void getAndSetDevice() {
        RegularLight r = new RegularLight();
        booleanTrigger.setDevice(r);
        assertEquals(r, booleanTrigger.getDevice());
    }

    @Test
    @DisplayName("get and set device id")
    public void getAndSetDeviceId() {
        booleanTrigger.setDeviceId(20L);
        assertEquals(20, booleanTrigger.getDeviceId());
    }

    @Test
    @DisplayName("get and set automation")
    public void getAndSetAutomation() {
        Automation r = new Automation();
        booleanTrigger.setAutomation(r);
        assertEquals(r, booleanTrigger.getAutomation());
    }

    @Test
    @DisplayName("get and set automation id")
    public void getAndSetAutomationId() {
        booleanTrigger.setAutomationId(20L);
        assertEquals(20, booleanTrigger.getAutomationId());
    }
}
