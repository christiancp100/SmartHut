package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

/** A 'dim' event from a button dimmer. */
@Data
public class ButtonDimmerDimRequest {

    /** The device id */
    @NotNull private Long id;

    public enum DimType {
        UP,
        DOWN;
    }

    /** Whether the dim is up or down */
    @NotNull private DimType dimType;
}
