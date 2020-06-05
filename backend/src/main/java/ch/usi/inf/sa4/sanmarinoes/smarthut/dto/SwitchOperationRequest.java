package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

/** An on/off/toggle operation on a switch */
@Data
public class SwitchOperationRequest {

    /** The device id */
    @NotNull private Long id;

    public enum OperationType {
        ON,
        OFF,
        TOGGLE
    }

    /** The type of switch operation */
    @NotNull private SwitchOperationRequest.OperationType type;
}
