package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Sensor;
import java.math.BigDecimal;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorSaveRequest {
    /** The type of this sensor */
    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Sensor.SensorType sensor;

    @NotNull private BigDecimal value;

    /**
     * The room this device belongs in, as a foreign key id. To use when updating and inserting from
     * a REST call.
     */
    @NotNull private Long roomId;

    /** The name of the device as assigned by the user (e.g. 'Master bedroom light') */
    @NotNull private String name;
}
