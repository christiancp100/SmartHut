package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Icon;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SceneSaveRequest {

    /** Room identifier */
    private long id;

    /** The user given name of this room (e.g. 'Master bedroom') */
    @NotNull private String name;

    @NotNull private Icon icon;

    /** Determines whether a guest can access this scene */
    @Column @NotNull private boolean guestAccessEnabled;
}
