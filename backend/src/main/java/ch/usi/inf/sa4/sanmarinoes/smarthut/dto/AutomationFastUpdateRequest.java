package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation.ConditionDTO;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation.ScenePriorityDTO;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation.TriggerDTO;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AutomationFastUpdateRequest {
    @NotNull private List<ScenePriorityDTO> scenes;
    @NotNull private List<TriggerDTO> triggers;
    @NotNull private List<ConditionDTO> conditions;
    @NotNull private long id;
    @NotNull @NotEmpty private String name;
}
