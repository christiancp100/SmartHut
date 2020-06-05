package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.DimmableLight;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.DimmableState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Dimmable State Tests")
public class DimmableStateTests {

    private DimmableState dimmableState;

    @BeforeEach
    public void createDimmableState() {
        dimmableState = new DimmableState();
    }

    @Test
    @DisplayName("get and set intensity")
    public void getAndSetIntensity() {
        this.dimmableState.setIntensity(20);
        assertEquals(20, this.dimmableState.getIntensity());
    }

    @Test
    @DisplayName("apply")
    public void apply() {
        DimmableLight d = new DimmableLight();
        d.setIntensity(45);
        this.dimmableState.setDevice(d);
        this.dimmableState.setIntensity(30);
        this.dimmableState.apply();
        assertEquals(30, d.getIntensity());
    }
}
