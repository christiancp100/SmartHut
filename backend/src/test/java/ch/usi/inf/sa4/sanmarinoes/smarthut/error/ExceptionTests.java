package ch.usi.inf.sa4.sanmarinoes.smarthut.error;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Exception tests")
public class ExceptionTests {
    @Test
    public void testBadData() {
        try {
            throw new BadDataException("message");
        } catch (BadDataException e) {
            assertEquals("message", e.getMessage());
        }
    }

    @Test
    public void testDuplicateRegistration() {
        try {
            throw new DuplicateRegistrationException();
        } catch (DuplicateRegistrationException e) {
            assertEquals("Email or username already belonging to another user", e.getMessage());
        }
    }

    @Test
    public void testDuplicateState() {
        try {
            throw new DuplicateStateException();
        } catch (DuplicateStateException e) {
            assertEquals(
                    "Cannot create state since it has already been created for this scene and this device",
                    e.getMessage());
        }
    }

    @Test
    public void testEmailTokenNotFound() {
        try {
            throw new EmailTokenNotFoundException();
        } catch (EmailTokenNotFoundException e) {
            assertEquals("Email verification token not found in DB", e.getMessage());
        }
    }

    @Test
    public void testNotFound() {
        try {
            throw new NotFoundException();
        } catch (NotFoundException e) {
            assertEquals("Not found", e.getMessage());
        }

        try {
            throw new NotFoundException("message");
        } catch (NotFoundException e) {
            assertEquals("message not found", e.getMessage());
        }
    }

    @Test
    public void testUserNotFound() {
        try {
            throw new UserNotFoundException();
        } catch (UserNotFoundException e) {
            assertEquals("No user found with given email", e.getMessage());
        }
    }
}
