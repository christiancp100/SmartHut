package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.DimmableLight;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.KnobDimmer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Dimmer Tests")
public class DimmerTests {

    private KnobDimmer dimmer;

    @BeforeEach
    public void createDimmer() {
        dimmer = new KnobDimmer();
    }

    @Test
    @DisplayName("connect true")
    public void connectTrue() {
        DimmableLight d = new DimmableLight();
        dimmer.connect(d, true);

        assertTrue(d.getDimmers().contains((this.dimmer)));

        assertTrue((this.dimmer.getOutputs().contains(d)));
    }

    @Test
    @DisplayName("connect off")
    public void connectOff() {
        DimmableLight d = new DimmableLight();
        d.getDimmers().add(this.dimmer);
        dimmer.getOutputs().add(d);
        dimmer.connect(d, false);

        assertFalse(d.getDimmers().contains((this.dimmer)));

        assertFalse((this.dimmer.getOutputs().contains(d)));
    }
}
