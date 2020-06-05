package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTResponse {
    private final String jwttoken;
}
