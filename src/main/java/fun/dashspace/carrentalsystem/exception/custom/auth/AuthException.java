package fun.dashspace.carrentalsystem.exception.custom.auth;

import fun.dashspace.carrentalsystem.exception.custom.CustomException;
import org.springframework.http.HttpStatus;

public abstract class AuthException extends CustomException {
    protected AuthException(String message, HttpStatus status, String error) {
        super(message, status, error);
    }

    protected AuthException(String message, HttpStatus status, String error, Throwable cause) {
        super(message, status, error, cause);
    }
}
