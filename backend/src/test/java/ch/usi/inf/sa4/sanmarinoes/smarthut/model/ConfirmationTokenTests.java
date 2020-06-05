package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.*;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ConfirmationToken;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.User;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ConfirmationToken tests")
public class ConfirmationTokenTests {
    private ConfirmationToken token;

    @BeforeEach
    private void createToke() {
        User user = new User();
        user.setName("Tizio Sempronio");
        user.setId(42l);
        user.setUsername("xXCoolUserNameXx");
        user.setEmail("realMail@service.ext");
        user.setPassword("alpaca");
        this.token = new ConfirmationToken(user);
    }

    @Test
    @DisplayName("test setId")
    public void testSetId() {
        this.token.setId(34l);
        assertEquals(34l, token.getId());
    }

    @Test
    @DisplayName("test getUSerId")
    public void testGetUserId() {
        assertEquals(42l, token.getUser().getId());
    }

    @Test
    @DisplayName("test setResetPassword")
    public void testSetResetPassword() {
        assertFalse(token.isResetPassword());
        token.setResetPassword(true);
        assertTrue(token.isResetPassword());
    }

    @Test
    @DisplayName("test setTimeStamp")
    public void testSetTimeStamp() {
        Date date = new Date();
        date.setTime(86400000l);
        token.setCreatedDate(date);
        assertEquals(date, token.getCreatedDate());
    }

    @Test
    @DisplayName("test setConfirmToken")
    public void testSetConfirmTOken() {
        assertNotEquals(null, token.getConfirmToken());
        String newToken = "newConfirmationToken";
        token.setConfirmToken(newToken);
        assertEquals(newToken, token.getConfirmToken());
    }
}
