package ch.usi.inf.sa4.sanmarinoes.smarthut.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends Exception {
    public NotFoundException() {
        super("Not found");
    }

    public NotFoundException(String what) {
        super(what + " not found");
    }
}
