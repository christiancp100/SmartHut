package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.RegularLight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("A RegularLight")
public class RegularLightTests {

    RegularLight regularLight;

    @BeforeEach
    public void createRegularLight() {
        this.regularLight = new RegularLight();
    }

    @Test
    @DisplayName("State when just created")
    public void beginningState() {
        assertFalse(regularLight.isOn());
    }

    @Test
    @DisplayName("Changing state to on after creating the light")
    public void createAndSetOn() {
        regularLight.setOn(true);
        assertTrue(regularLight.isOn());
    }

    @Test
    @DisplayName("Change state of the light to off after creating it")
    public void createAndSetOff() {
        regularLight.setOn(false);
        assertFalse(regularLight.isOn());
    }

    @Test
    @DisplayName("Checks whether a turned on light getting turned on is still in the on State")
    public void setOn() {
        regularLight.setOn(true);
        regularLight.setOn(true);
        assertTrue(regularLight.isOn());
    }
}
