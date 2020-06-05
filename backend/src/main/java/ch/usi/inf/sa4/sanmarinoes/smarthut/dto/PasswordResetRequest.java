package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/** DTO for password reset request */
@Data
public class PasswordResetRequest {

    @NotNull private String confirmationToken;

    /** A properly salted way to store the password */
    @NotNull
    @NotEmpty(message = "Please provide a password")
    @Size(
            min = 6,
            max = 255,
            message = "Your password should be at least 6 characters long and up to 255 chars long")
    private String password;
}
