package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BooleanConditionOrTriggerSaveRequest {

    private long id;

    @NotNull private Long deviceId;

    @NotNull private Long automationId;

    private boolean on;
}
