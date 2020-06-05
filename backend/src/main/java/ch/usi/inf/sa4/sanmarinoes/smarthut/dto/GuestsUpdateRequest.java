package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GuestsUpdateRequest {
    @NotNull private List<Long> ids;
}
