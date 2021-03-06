package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GenericDeviceSaveRequest {
    /**
     * The room this device belongs in, as a foreign key id. To use when updating and inserting from
     * a REST call.
     */
    @NotNull private Long roomId;

    /** The name of the device as assigned by the user (e.g. 'Master bedroom light') */
    @NotNull private String name;
}
