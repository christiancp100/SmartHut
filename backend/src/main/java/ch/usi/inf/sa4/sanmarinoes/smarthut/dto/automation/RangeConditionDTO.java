package ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Condition;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Operator;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.RangeCondition;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class RangeConditionDTO extends ConditionDTO {

    @NotNull @Getter @Setter private Operator operator;
    @NotNull @Getter @Setter private double range;

    @Override
    public Condition<?> toModel() {
        RangeCondition t = new RangeCondition();
        t.setDeviceId(this.getDeviceId());
        t.setOperator(this.operator);
        t.setRange(this.range);
        return t;
    }
}
