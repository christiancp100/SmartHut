package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("switchableStateSaveRequest tests")
public class SwitchableStateSaveRequestTests {
    private SwitchableStateSaveRequest saveRequest;

    @BeforeEach
    public void createSwitchableStateSaveRequest() {
        this.saveRequest = new SwitchableStateSaveRequest();
    }

    @Test
    @DisplayName("test isOn()")
    public void isOnTest() {
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
        assertEquals(null, saveRequest.getId());
    }
}
