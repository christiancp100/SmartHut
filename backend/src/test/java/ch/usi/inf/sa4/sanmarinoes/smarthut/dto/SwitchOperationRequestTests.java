package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("switchOperationRequest tests")
public class SwitchOperationRequestTests {
    private SwitchOperationRequest request;

    @BeforeEach
    private void createRequest() {
        this.request = new SwitchOperationRequest();
    }

    @Test
    @DisplayName("test setId")
    public void testSetId() {
        request.setId(42L);
        assertEquals(42L, request.getId());
    }

    @Test
    @DisplayName("test setType")
    public void testSetTypeOFF() {
        request.setType(SwitchOperationRequest.OperationType.OFF);
        assertEquals(SwitchOperationRequest.OperationType.OFF, request.getType());
    }

    @Test
    @DisplayName("test setType")
    public void testSetTypeON() {
        request.setType(SwitchOperationRequest.OperationType.ON);
        assertEquals(SwitchOperationRequest.OperationType.ON, request.getType());
    }

    @Test
    @DisplayName("test setType")
    public void testSetTypeTOGGLE() {
        request.setType(SwitchOperationRequest.OperationType.TOGGLE);
        assertEquals(SwitchOperationRequest.OperationType.TOGGLE, request.getType());
    }
}
