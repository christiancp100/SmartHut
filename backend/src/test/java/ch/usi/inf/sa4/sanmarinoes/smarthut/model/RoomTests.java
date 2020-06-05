package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.DimmableLight;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Icon;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Room;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Room test")
public class RoomTests {
    private Room room;

    @BeforeEach
    private void createRoom() {
        room = new Room();
    }

    @Test
    @DisplayName("test id")
    public void testId() {
        room.setId(42l);
        assertEquals(42l, room.getId());
    }

    @Test
    @DisplayName("test userId")
    public void testUserId() {
        room.setUserId(42l);
        assertEquals(42l, room.getUserId());
    }

    @Test
    @DisplayName("test name")
    public void testName() {
        room.setName("alpaca");
        assertEquals("alpaca", room.getName());
    }

    @Test
    @DisplayName("test image")
    public void testImage() {
        room.setImage("realFakeImage.png");
        assertEquals("realFakeImage.png", room.getImage());
    }

    @Test
    @DisplayName("test toString()")
    public void testToString() {
        room.setId(1l);
        room.setName("alpaca");
        assertEquals("Room{id=1, name='alpaca'}", room.toString());
    }

    @Test
    @DisplayName("test devices")
    public void testDevices() {
        room.getDevices().add(new DimmableLight());
        assertEquals(1, room.getDevices().size());
    }

    @Test
    @DisplayName("test user")
    public void testUser() {
        User user = new User();
        user.setId(34l);
        room.setUser(user);
        assertEquals(34l, room.getUser().getId());
    }

    @Test
    @DisplayName("test Icon")
    public void testIcon() {
        // ImageIcon image = new ImageIcon("file","description");
        room.setIcon(Icon.FEMALE);
        assertEquals("female", room.getIcon().toString());
    }
}
