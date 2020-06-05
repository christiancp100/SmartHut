package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SwitchableStateSaveRequest {

    /** Device id (used only for update requests) */
    @NotNull private Long id;

    @NotNull private boolean on;
}
