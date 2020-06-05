package ch.usi.inf.sa4.sanmarinoes.smarthut.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends Exception {
    private final boolean isUserDisabled;

    public UnauthorizedException(boolean isDisabled, Throwable cause) {
        super("Access denied: " + (isDisabled ? "user is disabled" : "wrong credentials"), cause);
        this.isUserDisabled = isDisabled;
    }

    public boolean isUserDisabled() {
        return isUserDisabled;
    }
}
