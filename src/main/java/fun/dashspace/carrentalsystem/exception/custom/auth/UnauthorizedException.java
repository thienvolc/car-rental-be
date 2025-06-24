package fun.dashspace.carrentalsystem.exception.custom.auth;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends AuthException {
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;
    private static final String ERROR = "Unauthorized";

    public UnauthorizedException(String message) {
        super(message, STATUS, ERROR);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, STATUS, ERROR, cause);
    }
}
