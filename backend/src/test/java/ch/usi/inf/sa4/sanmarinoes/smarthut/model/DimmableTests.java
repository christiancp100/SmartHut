package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Dimmable tests")
public class DimmableTests {
    private DimmableLight dimmable;

    @BeforeEach
    private void createDimmable() {
        dimmable = new DimmableLight();
        dimmable.setId(42l);
        KnobDimmer dimmer1 = new KnobDimmer();
        dimmer1.setLightIntensity(75);
        ButtonDimmer dimmer2 = new ButtonDimmer();
        dimmer2.setName("button");
        Set<Dimmer> set = new HashSet<Dimmer>();
        set.add(dimmer1);
        set.add(dimmer2);
        dimmable.setDimmers(set);
    }

    @Test
    @DisplayName("test getDimmers")
    public void testGetDimmers() {
        assertEquals(2, dimmable.getDimmers().size());
    }

    @Test
    @DisplayName("test intensity and old Intensity")
    public void testIntensities() {
        assertEquals(0, dimmable.getIntensity());
        dimmable.setIntensity(13);
        dimmable.setIntensity(24);
        assertEquals(24, dimmable.getIntensity());
        assertEquals(24, dimmable.getOldIntensity());
        dimmable.setOldIntensity(99);
        assertEquals(99, dimmable.getOldIntensity());
    }

    @Test
    @DisplayName("test setOn")
    public void testIsOn() {
        assertFalse(dimmable.isOn());
        dimmable.setOn(true);
        assertTrue(dimmable.isOn());
        assertNotEquals(0, dimmable.getIntensity());
    }

    @Test
    @DisplayName("test readStateAndSet")
    public void testReadStateAndSet() {
        DimmableState state = new DimmableState();
        state.setIntensity(78);
        dimmable.readStateAndSet(state);
        assertEquals(78, dimmable.getIntensity());
        assertEquals(78, dimmable.getOldIntensity());
    }

    @Test
    @DisplayName("test cloneState")
    public void testCloneState() {
        dimmable.setId(27);
        DimmableState state = (DimmableState) dimmable.cloneState();
        assertEquals(state.getDeviceId(), dimmable.getId());
        assertEquals(state.getIntensity(), dimmable.getIntensity());
    }

    @Test
    @DisplayName("test readTriggerState")
    public void testReadTriggerState() {
        dimmable.setIntensity(84);
        assertEquals(84, dimmable.readTriggerState());
    }
}
