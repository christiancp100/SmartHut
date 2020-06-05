package ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Trigger;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public abstract class TriggerDTO {
    @NotNull @Getter @Setter private long deviceId;

    public abstract Trigger<?> toModel();
}
