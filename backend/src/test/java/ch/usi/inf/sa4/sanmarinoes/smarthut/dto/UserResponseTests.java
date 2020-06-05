package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("test UserResponse")
public class UserResponseTests {
    private UserResponse response;
    private User user;

    @BeforeEach
    private void createUser() {
        user = new User();
        user.setName("John RealName");
        user.setId(29l);
        user.setUsername("pseudonym");
        response = response.fromUser(user);
    }

    @Test
    @DisplayName("test getId")
    public void testGetId() {
        assertEquals(29l, response.getId());
    }

    @Test
    @DisplayName("test getUsername")
    public void testGetUsername() {
        assertEquals("pseudonym", response.getUsername());
    }

    @Test
    @DisplayName("test getName")
    public void testGetaName() {
        assertEquals("John RealName", response.getName());
    }
}
