package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("A switch")
public class SwitchTests {

    Switch aSwitch;

    @BeforeEach
    public void createNewSwitch() {

        this.aSwitch = new Switch();
        RegularLight regularLight = new RegularLight();
        DimmableLight dimmableLight = new DimmableLight();
        SmartPlug smartPlug = new SmartPlug();
        this.aSwitch.getOutputs().add(regularLight);
        this.aSwitch.getOutputs().add(dimmableLight);
        this.aSwitch.getOutputs().add(smartPlug);
    }

    @Test
    @DisplayName("check state when switch created")
    public void createdSwitch() {
        assertFalse(aSwitch.isOn());
    }

    @Test
    @DisplayName("Check toggle on a switch in its off state")
    public void offToggle() {
        aSwitch.toggle();
        assertTrue(aSwitch.isOn());
    }

    @Test
    @DisplayName("Checks whether setting on a off switch works as intended")
    public void offSetOn() {
        aSwitch.setOn(true);
        assertTrue(aSwitch.isOn());
    }

    @Test
    @DisplayName("Checks whether setting off a on switch works as intended")
    public void onSetOff() {
        aSwitch.toggle();
        aSwitch.setOn(false);
        assertFalse(aSwitch.isOn());
    }

    @Test
    @DisplayName("Checks that setting off an off switch results in a off state")
    public void offSetOff() {
        aSwitch.setOn(false);
        assertFalse(aSwitch.isOn());
    }

    @Test
    @DisplayName("Checks that setting on an on switch results in a on state")
    public void onSetOn() {
        aSwitch.toggle();
        aSwitch.setOn(true);
        assertTrue(aSwitch.isOn());
    }

    @Test
    @DisplayName("Checks wheter toggling a on switch set its state to off")
    public void onToggle() {
        aSwitch.setOn(true);
        aSwitch.toggle();
        assertFalse(aSwitch.isOn());
    }

    @Test
    @DisplayName("Checks that toggling on sets all elements of the Set on as well")
    public void toggleEffctOnSet() {
        aSwitch.toggle();
        for (final Switchable s : aSwitch.getOutputs()) {
            assertTrue(s.isOn());
        }
    }

    @Test
    @DisplayName("Checks that toggling the switch off also sets all elements of its set off")
    public void toggleOffEffectOnElementes() {
        aSwitch.setOn(true);
        aSwitch.toggle();
        for (final Switchable s : aSwitch.getOutputs()) {
            assertFalse(s.isOn());
        }
    }
}
