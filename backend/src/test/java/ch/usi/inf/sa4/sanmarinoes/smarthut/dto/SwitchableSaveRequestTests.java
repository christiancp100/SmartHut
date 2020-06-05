package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SwitchableSaveRequest tests")
public class SwitchableSaveRequestTests {

    private SwitchableSaveRequest saveRequest;

    @BeforeEach
    private void createSaveRequest() {
        this.saveRequest = new SwitchableSaveRequest();
    }

    @Test
    @DisplayName("test setRoomId")
    public void testSetRoomId() {
        saveRequest.setRoomId(42L);
        assertEquals(42L, saveRequest.getRoomId());
    }

    @Test
    @DisplayName("test setName")
    public void testSetName() {
        saveRequest.setName("Giovanni");
        assertEquals("Giovanni", saveRequest.getName());
    }

    @Test
    @DisplayName("test isOn()")
    public void inOnTest() {
        assertFalse(saveRequest.isOn());
    }

    @Test
    @DisplayName("test setOn(true) ")
    public void setOnTestTrue() {
        saveRequest.setOn(true);
        assertTrue(saveRequest.isOn());
    }

    @Test
    @DisplayName("test setOn(false) ")
    public void setOnTestFalse() {
        saveRequest.setOn(false);
        assertFalse(saveRequest.isOn());
    }

    @Test
    @DisplayName("test setId")
    public void testSetId() {
        saveRequest.setId(300771L);
        assertEquals(300771L, saveRequest.getId());
    }
}
