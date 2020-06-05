package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Dimmable;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.DimmableLight;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.KnobDimmer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("A Knob Dimmer")
public class KnobDimmerTests {

    KnobDimmer knobDimmer;

    @BeforeEach
    public void createNewKnobDimmer() {
        this.knobDimmer = new KnobDimmer();
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
                knobDimmer.addDimmable(dl);
            }
        }

        @Test
        @DisplayName(" set the intensity ")
        public void increase() {
            knobDimmer.setLightIntensity(30);
            for (Dimmable dl : knobDimmer.getOutputs()) {
                assertEquals(30, dl.getIntensity());
            }
        }
    }
}
