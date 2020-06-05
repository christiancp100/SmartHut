package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.Data;

/** DTO for password reset request */
@Data
public class InitPasswordResetRequest {
    /**
     * The user's email (validated according to criteria used in <code>&gt;input type="email"&lt;>
     * </code>, technically not RFC 5322 compliant
     */
    @NotEmpty(message = "Please provide an email")
    @Email(message = "Please provide a valid email address")
    @Pattern(regexp = ".+@.+\\..+", message = "Please provide a valid email address")
    private String email;
}
