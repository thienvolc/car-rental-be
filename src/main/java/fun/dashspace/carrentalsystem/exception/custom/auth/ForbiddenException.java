package fun.dashspace.carrentalsystem.exception.custom.auth;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends AuthException {

    private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;
    private static final String ERROR = "Forbidden";

    public ForbiddenException(String message) {
        super(message, STATUS, ERROR);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, STATUS, ERROR, cause);
    }
}
