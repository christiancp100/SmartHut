package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ScenePriority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Scene Priority Tests")
public class ScenePriorityTests {

    private ScenePriority scenePriority;

    @BeforeEach
    public void scenePriorityCreate() {
        this.scenePriority = new ScenePriority();
    }

    @Test
    @DisplayName("get and set automation id")
    public void getAndSetAutomationId() {
        scenePriority.setAutomationId(20L);

        assertEquals(20, scenePriority.getAutomationId());
    }

    @Test
    @DisplayName("get and set scene id")
    public void getAndSetSceneId() {
        scenePriority.setSceneId(20L);

        assertEquals(20, scenePriority.getSceneId());
    }

    @Test
    @DisplayName("get and set priority")
    public void getAndSetPriority() {
        scenePriority.setPriority(20);

        assertEquals(20, scenePriority.getPriority());
    }
}
