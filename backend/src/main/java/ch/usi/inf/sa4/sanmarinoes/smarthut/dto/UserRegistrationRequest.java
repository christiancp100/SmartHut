package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegistrationRequest {

    /** The full name of the user */
    @NotNull
    @NotEmpty(message = "Please provide a full name")
    private String name;

    /** The full name of the user */
    @NotNull
    @NotEmpty(message = "Please provide a username")
    @Pattern(
            regexp = "[A-Za-z0-9_\\-]+",
            message = "Username can contain only letters, numbers, '_' and '-'")
    private String username;

    /** A properly salted way to store the password */
    @NotNull
    @NotEmpty(message = "Please provide a password")
    @Size(
            min = 6,
            max = 255,
            message = "Your password should be at least 6 characters long and up to 255 chars long")
    private String password;

    /**
     * The user's email (validated according to criteria used in <code>&gt;input type="email"&lt;>
     * </code>, technically not RFC 5322 compliant
     */
    @NotNull
    @NotEmpty(message = "Please provide an email")
    @Email(message = "Please provide a valid email address")
    @Pattern(regexp = ".+@.+\\..+", message = "Please provide a valid email address")
    private String email;
}
