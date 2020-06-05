package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.SecurityCamera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Security Camera Tests")
public class SecurityCameraTests {

    private SecurityCamera securityCamera;

    @BeforeEach
    public void createSecurityCamera() {
        securityCamera = new SecurityCamera();
    }

    @Test
    @DisplayName("get and set Path")
    public void getAndSetPath() {
        securityCamera.setPath("ciao mamma");

        assertEquals("ciao mamma", securityCamera.getPath());
    }

    @Test
    @DisplayName("get and set On")
    public void getAndSetOn() {
        securityCamera.setOn(true);

        assertTrue(securityCamera.isOn());
    }

    @Test
    @DisplayName("trigger state")
    public void triggerState() {

        securityCamera.setOn(true);

        assertTrue(securityCamera.readTriggerState());
    }
}
