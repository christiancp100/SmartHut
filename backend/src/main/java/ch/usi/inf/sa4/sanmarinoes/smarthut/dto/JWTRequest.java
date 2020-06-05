package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JWTRequest {
    @NotNull private String usernameOrEmail;
    @NotNull private String password;
}
