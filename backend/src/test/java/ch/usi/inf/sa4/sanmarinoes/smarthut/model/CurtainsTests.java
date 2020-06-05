package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Curtains;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Curtains tests")
public class CurtainsTests {
    private Curtains curtains;

    @BeforeEach
    public void createCurtains() {
        this.curtains = new Curtains();
    }

    @Test
    @DisplayName("State when just created")
    public void initialState() {
        assertEquals(0, this.curtains.getIntensity());
    }

    @Test
    @DisplayName("Check wether setting the opening works")
    public void normalSet() {
        this.curtains.setIntensity(42);
        assertEquals(42, this.curtains.getIntensity());
    }

    @Test
    @DisplayName("Set setting a negative number")
    public void setNeg() {
        this.curtains.setIntensity(-1);
        assertEquals(0, this.curtains.getIntensity());
    }

    @Test
    @DisplayName("Setting state to a number greater than 100")
    public void setLarge() {
        this.curtains.setIntensity(32768);
        assertEquals(100, this.curtains.getIntensity());
    }
}
