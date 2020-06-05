package ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Operator;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.RangeTrigger;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Trigger;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class RangeTriggerDTO extends TriggerDTO {
    @NotNull @Getter @Setter private Operator operator;
    @NotNull @Getter @Setter private double range;

    @Override
    public Trigger<?> toModel() {
        RangeTrigger t = new RangeTrigger();
        t.setDeviceId(this.getDeviceId());
        t.setOperator(this.operator);
        t.setRange(this.range);
        return t;
    }
}
