package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DimmableSaveRequest {

    /** Device id (used only for update requests) */
    private long id;

    /** The light intensity value. Goes from 0 (off) to 100 (on) */
    @NotNull
    @Min(0)
    @Max(100)
    private Integer intensity = 0;

    /**
     * The room this device belongs in, as a foreign key id. To use when updating and inserting from
     * a REST call.
     */
    @NotNull private Long roomId;

    /** The name of the device as assigned by the user (e.g. 'Master bedroom light') */
    @NotNull private String name;
}
