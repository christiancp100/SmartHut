package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ButtonDimmer;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Dimmable;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.DimmableLight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("A Button Dimmer")
public class ButtonDimmerTests {

    ButtonDimmer buttonDimmer;

    @BeforeEach
    public void createNewButtonDimmer() {
        this.buttonDimmer = new ButtonDimmer();
    }

    @Nested
    @DisplayName(" when multiple lights are present")
    class MultipleLights {

        @BeforeEach
        public void setLights() {
            DimmableLight dl;
            for (int i = 0; i < 3; i++) {
                dl = new DimmableLight();
                dl.setIntensity(10);
                ;
                buttonDimmer.addDimmable(dl);
            }
        }

        @Test
        @DisplayName(" increase the intensity ")
        public void increase() {
            buttonDimmer.increaseIntensity();
            for (Dimmable dl : buttonDimmer.getOutputs()) {
                assertTrue(dl.getIntensity() > 10);
            }
        }

        @Test
        @DisplayName(" decrease the intensity ")
        public void decrease() {
            buttonDimmer.decreaseIntensity();
            for (Dimmable dl : buttonDimmer.getOutputs()) {
                assertTrue(dl.getIntensity() < 10);
            }
        }
    }
}
