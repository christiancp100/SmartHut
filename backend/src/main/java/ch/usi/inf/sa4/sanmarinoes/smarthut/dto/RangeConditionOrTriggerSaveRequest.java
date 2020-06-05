package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Operator;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RangeConditionOrTriggerSaveRequest {

    private long id;

    @NotNull private Long deviceId;

    @NotNull private Long automationId;

    @NotNull private Operator operator;

    @NotNull private double range;
}
