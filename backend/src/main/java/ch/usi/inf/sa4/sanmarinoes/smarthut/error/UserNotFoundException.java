package ch.usi.inf.sa4.sanmarinoes.smarthut.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("No user found with given email");
    }
}
