package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DimmableStateSaveRequest {

    /** Device id (used only for update requests) */
    @NotNull private Long id;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer intensity = 0;
}
