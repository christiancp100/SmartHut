package ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.BooleanTrigger;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Trigger;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class BooleanTriggerDTO extends TriggerDTO {
    @NotNull @Getter @Setter private boolean on;

    @Override
    public Trigger<?> toModel() {
        BooleanTrigger t = new BooleanTrigger();
        t.setDeviceId(this.getDeviceId());
        t.setOn(this.on);
        return t;
    }
}
