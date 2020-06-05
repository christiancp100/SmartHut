package ch.usi.inf.sa4.sanmarinoes.smarthut.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DuplicateRegistrationException extends Exception {
    public DuplicateRegistrationException() {
        super("Email or username already belonging to another user");
    }
}
