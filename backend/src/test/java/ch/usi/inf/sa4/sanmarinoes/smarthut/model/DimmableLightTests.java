package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.DimmableLight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("A Button Dimmer")
public class DimmableLightTests {

    DimmableLight dimmableLight;

    @BeforeEach
    public void createNewButtonDimmer() {
        this.dimmableLight = new DimmableLight();
    }

    @Nested
    @DisplayName(" when on")
    class WhenOn {

        @BeforeEach
        public void setLightOn() {
            dimmableLight.setOn(true);
        }

        @Test
        @DisplayName("the isOn method should return true")
        public void isOn() {
            assertTrue(dimmableLight.isOn());
        }

        @Test
        @DisplayName("the intensity should be 100 when the light is turned on")
        public void checkIntensityWhenTurnedOn() {
            assertEquals(100, dimmableLight.getIntensity());
        }

        @Test
        @DisplayName("setting the intensity to a number between 1 and 100")
        public void checkIntensityBetweenLimits() {
            dimmableLight.setIntensity(50);
            assertEquals(50, dimmableLight.getIntensity());
        }

        @Test
        @DisplayName("setting the intensity to a number > 100")
        public void checkIntensityMoreThanLimits() {
            dimmableLight.setIntensity(150);
            assertEquals(100, dimmableLight.getIntensity());
        }

        @Test
        @DisplayName("setting the intensity to a number < 0")
        public void checkIntensityLessThanLimits() {
            dimmableLight.setIntensity(-30);
            assertEquals(0, dimmableLight.getIntensity());
        }

        @Test
        @DisplayName("setting the intensity to a number <= 0 should turn the light off")
        public void checkTurnOn() {
            dimmableLight.setIntensity(0);
            assertFalse(dimmableLight.isOn());
        }
    }

    @Nested
    @DisplayName(" when off")
    class WhenOff {

        @BeforeEach
        public void setLightOff() {
            dimmableLight.setOn(false);
        }

        @Test
        @DisplayName("the isOn method should return false")
        public void isOn() {
            assertFalse(dimmableLight.isOn());
        }

        @Test
        @DisplayName("the intensity should be 0 when the light is turned off")
        public void checkIntensityWhenTurnedOff() {
            assertEquals(0, dimmableLight.getIntensity());
        }

        @Test
        @DisplayName("setting the intensity to a number between 1 and 100")
        public void checkIntensityBetweenLimits() {
            dimmableLight.setIntensity(50);
            assertEquals(50, dimmableLight.getIntensity());
        }

        @Test
        @DisplayName("setting the intensity to a number > 0 should turn the light on")
        public void checkIntensityLessThanLimits() {
            dimmableLight.setIntensity(47);
            assertEquals(47, dimmableLight.getIntensity());
        }
    }
}
