package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScenePrioritySaveRequest {

    @NotNull private Long automationId;

    @Min(0)
    private Integer priority;

    @NotNull private Long sceneId;
}
