package ch.usi.inf.sa4.sanmarinoes.smarthut.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class EmailTokenNotFoundException extends Exception {
    public EmailTokenNotFoundException() {
        super("Email verification token not found in DB");
    }
}
