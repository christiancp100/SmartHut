package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Icon;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomSaveRequest {

    /** Room identifier */
    private long id;

    @NotNull private Icon icon;

    /**
     * Image is to be given as byte[]. In order to get an encoded string from it, the
     * Base64.getEncoder().encodeToString(byte[] content) should be used. For further information:
     * https://www.baeldung.com/java-base64-image-string
     * https://docs.oracle.com/javase/8/docs/api/java/util/Base64.html
     */
    @Lob private String image;

    /** The user given name of this room (e.g. 'Master bedroom') */
    @NotNull private String name;
}
