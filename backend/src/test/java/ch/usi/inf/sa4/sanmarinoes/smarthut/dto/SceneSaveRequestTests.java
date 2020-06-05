package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Icon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SceneSaveRequest tests")
public class SceneSaveRequestTests {

    private SceneSaveRequest scene;

    @BeforeEach
    private void createSceneSaveRequest() {
        scene = new SceneSaveRequest();
    }

    @Test
    @DisplayName("guestAccessEnable")
    public void testGuestAccess() {
        assertFalse(scene.isGuestAccessEnabled());
    }

    @Test
    @DisplayName("set guestAccess")
    public void testSetGuestAccess() {
        scene.setGuestAccessEnabled(true);
        assertTrue(scene.isGuestAccessEnabled());
    }

    @Test
    @DisplayName("test getId")
    public void testGetId() {
        assertEquals(0L, scene.getId());
    }

    @Test
    @DisplayName("test getName")
    public void testGetName() {
        scene.setName("Roberto");
        assertEquals("Roberto", scene.getName());
    }

    @Test
    @DisplayName("test getIcon")
    public void testGetIcon() {
        scene.setIcon(Icon.FEMALE);
        assertEquals("female", scene.getIcon().toString());
    }
}
