package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName(" USer Tests")
public class UserTests {

    private User user;

    @BeforeEach
    public void createUser() {
        user = new User();
    }

    @Test
    @DisplayName("get and set id")
    public void getAndSetId() {
        user.setId(20L);

        assertEquals(20, user.getId());
    }

    @Test
    @DisplayName("get and set id")
    public void getAndSetName() {
        user.setName("Paolo Bitta");

        assertEquals("Paolo Bitta", user.getName());
    }

    @Test
    @DisplayName("get and set id")
    public void getAndSetUsername() {
        user.setUsername("PaulB");

        assertEquals("PaulB", user.getUsername());
    }

    @Test
    @DisplayName("get and set email")
    public void getAndSetEmail() {
        user.setEmail("paolo.bitta@gmail.com");

        assertEquals("paolo.bitta@gmail.com", user.getEmail());
    }

    @Test
    @DisplayName("get and set password")
    public void getAndSetPassword() {
        user.setPassword("cameraCaffe");

        assertEquals("cameraCaffe", user.getPassword());
    }

    @Test
    @DisplayName("get and set enabled")
    public void getAndSetEnabled() {
        user.setEnabled(true);

        assertTrue(user.isEnabled());
    }

    @Test
    @DisplayName("get and set Cameraenabled")
    public void getAndSeCameraEnabled() {
        user.setCameraEnabled(true);

        assertTrue(user.isCameraEnabled());
    }

    @Test
    @DisplayName("equals")
    public void eq() {
        assertNotEquals(null, user);

        assertEquals(user, user);
    }
}
