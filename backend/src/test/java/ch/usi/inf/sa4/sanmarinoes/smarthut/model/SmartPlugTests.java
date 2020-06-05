package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.SmartPlug;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SmartPlug Tests")
public class SmartPlugTests {

    private SmartPlug smartPlug;

    @BeforeEach
    public void createSmartPlug() {
        this.smartPlug = new SmartPlug();
    }

    @Test
    @DisplayName("set and get on")
    public void testOn() {
        smartPlug.setOn(true);

        assertTrue(smartPlug.isOn());
    }

    @Test
    @DisplayName("read trigger state")
    public void readTriggerState() {
        smartPlug.setOn(true);

        assertTrue(smartPlug.readTriggerState());
    }

    @Test
    @DisplayName("reset total consumption")
    public void reset() {

        assertEquals(new BigDecimal(0), smartPlug.getTotalConsumption());
    }
}
