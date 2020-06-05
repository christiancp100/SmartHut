package ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.BooleanCondition;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Condition;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class BooleanConditionDTO extends ConditionDTO {

    @NotNull @Getter @Setter private boolean on;

    @Override
    public Condition<?> toModel() {
        BooleanCondition t = new BooleanCondition();
        t.setDeviceId(this.getDeviceId());
        t.setOn(this.on);
        return t;
    }
}
