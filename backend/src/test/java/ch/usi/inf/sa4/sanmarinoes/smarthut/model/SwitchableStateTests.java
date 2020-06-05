package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.SwitchableState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Switchable State Tests")
public class SwitchableStateTests {

    private SwitchableState switchableState;

    @BeforeEach
    public void createSwitchableState() {
        switchableState = new SwitchableState();
    }

    @Test
    @DisplayName("is on")
    public void isOn() {
        switchableState.setOn(true);

        assertTrue(switchableState.isOn());
    }
}
