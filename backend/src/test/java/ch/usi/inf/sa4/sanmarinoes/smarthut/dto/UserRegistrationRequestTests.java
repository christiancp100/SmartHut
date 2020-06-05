package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("userRegistrationRequest tests")
public class UserRegistrationRequestTests {
    private UserRegistrationRequest request;

    @BeforeEach
    private void createRequest() {
        request = new UserRegistrationRequest();
    }

    @Test
    @DisplayName("test setName")
    public void testSetName() {
        request.setName("Tizio Sempronio");
        assertEquals("Tizio Sempronio", request.getName());
    }

    @Test
    @DisplayName("test setUserName")
    public void testUserName() {
        request.setUsername("xXDarkAngelCraftXx");
        assertEquals("xXDarkAngelCraftXx", request.getUsername());
    }

    @Test
    @DisplayName("test setPassword")
    public void testPassword() {
        request.setPassword("password123");
        assertEquals("password123", request.getPassword());
    }

    @Test
    @DisplayName("test setEmail")
    public void testEmail() {
        request.setEmail("fakemail@service.ussr");
        assertEquals("fakemail@service.ussr", request.getEmail());
    }
}
