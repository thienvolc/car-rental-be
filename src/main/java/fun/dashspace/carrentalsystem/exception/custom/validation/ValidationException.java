package fun.dashspace.carrentalsystem.exception.custom.validation;

import fun.dashspace.carrentalsystem.exception.custom.CustomException;
import org.springframework.http.HttpStatus;

public abstract class ValidationException extends CustomException {
    protected ValidationException(String message, HttpStatus status, String error) {
        super(message, status, error);
    }

    protected ValidationException(String message, HttpStatus status, String error, Throwable cause) {
        super(message, status, error, cause);
    }
}
