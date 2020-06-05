package ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Condition;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Thermostat;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ThermostatCondition;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class ThermostatConditionDTO extends ConditionDTO {

    @NotNull @Getter @Setter private ThermostatCondition.Operator operator;

    @NotNull @Getter @Setter private Thermostat.Mode mode;

    @Override
    public Condition<?> toModel() {

        ThermostatCondition t = new ThermostatCondition();

        t.setDeviceId(this.getDeviceId());

        t.setOperator(this.operator);

        t.setMode(this.mode);

        return t;
    }
}
