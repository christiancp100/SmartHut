package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ThermostatSaveRequest {

    /** Device identifier */
    private long id;

    /**
     * The room this device belongs in, as a foreign key id. To use when updating and inserting from
     * a REST call.
     */
    @NotNull private Long roomId;

    /** The name of the device as assigned by the user (e.g. 'Master bedroom light') */
    @NotNull private String name;

    /** Temperature to be reached */
    @NotNull private BigDecimal targetTemperature;

    @NotNull private boolean useExternalSensors;

    /** State of this thermostat */
    @NotNull private boolean turnOn;
}
