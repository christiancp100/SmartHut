package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Thermostat;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ThermostatCondition;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ThermostatConditionSaveRequest {
    @NotNull private long id;

    @NotNull private Long deviceId;

    @NotNull private Long automationId;

    @NotNull private ThermostatCondition.Operator operator;

    @NotNull private Thermostat.Mode mode;
}
