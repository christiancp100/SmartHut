package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.DimmableLight;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.DimmableState;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Scene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Curtains tests")
public class StateTests {

    private DimmableState state;

    @BeforeEach
    public void createState() {
        this.state = new DimmableState();
    }

    @Test
    @DisplayName("get and set device")
    public void getAndSetDevice() {
        DimmableLight d = new DimmableLight();
        this.state.setDevice(d);
        assertEquals(d, this.state.getDevice());
    }

    @Test
    @DisplayName("get and set device id")
    public void getAndSetDeviceId() {
        this.state.setDeviceId(30L);
        assertEquals(30, this.state.getDeviceId());
    }

    @Test
    @DisplayName("get and set scene")
    public void getAndSetScene() {
        Scene s = new Scene();
        this.state.setScene(s);
        assertEquals(s, this.state.getScene());
    }

    @Test
    @DisplayName("get and set sceneId")
    public void getAndSetSceneId() {
        this.state.setSceneId(50L);
        assertEquals(50, this.state.getSceneId());
    }
}
