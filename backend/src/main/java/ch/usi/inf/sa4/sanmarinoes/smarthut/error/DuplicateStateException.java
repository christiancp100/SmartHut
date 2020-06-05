package ch.usi.inf.sa4.sanmarinoes.smarthut.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DuplicateStateException extends Exception {
    public DuplicateStateException() {
        super(
                "Cannot create state since it has already been created for this scene and this device");
    }
}
