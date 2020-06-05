package ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ScenePriority;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class ScenePriorityDTO {
    @NotNull @Getter @Setter private long sceneId;

    @NotNull
    @Min(0)
    private @Getter @Setter Integer priority;

    public ScenePriority toModel() {
        ScenePriority s = new ScenePriority();
        s.setSceneId(sceneId);
        s.setPriority(priority);
        return s;
    }
}
