package fun.dashspace.carrentalsystem.exception.custom.auth;

import org.springframework.http.HttpStatus;

public class TokenException extends AuthException {

    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;
    private static final String ERROR = "Token Error";

    public TokenException(String message) {
        super(message, STATUS, ERROR);
    }

    public TokenException(String message, Throwable cause) {
        super(message, STATUS, ERROR, cause);
    }
}