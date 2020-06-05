package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Scene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Scene Tests")
public class SceneTests {

    private Scene scene;

    @BeforeEach
    public void createScene() {
        this.scene = new Scene();
    }

    @Test
    @DisplayName("get and set id")
    public void getAndSetId() {
        scene.setId(20L);

        assertEquals(20, scene.getId());
    }

    @Test
    @DisplayName("get and set user id")
    public void getAndSetUserId() {
        scene.setUserId(20L);

        assertEquals(20, scene.getUserId());
    }

    @Test
    @DisplayName("get and set name")
    public void getAndSetName() {
        scene.setName("ciao mamma");

        assertEquals("ciao mamma", scene.getName());
    }

    @Test
    @DisplayName("get access enabled")
    public void accessEnabled() {
        scene.setGuestAccessEnabled(true);

        assertTrue(scene.isGuestAccessEnabled());
    }
}
