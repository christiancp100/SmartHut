package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class KnobDimmerDimRequest {

    /** The device id */
    @NotNull private Long id;

    /** The absolute intensity value */
    @NotNull
    @Min(0)
    @Max(100)
    private Integer intensity;
}
