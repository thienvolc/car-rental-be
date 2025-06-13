package fun.dashspace.carrentalsystem.exception.custom.resource;

import fun.dashspace.carrentalsystem.exception.custom.CustomException;
import org.springframework.http.HttpStatus;

public abstract class ResourceException extends CustomException {

    protected ResourceException(String message, HttpStatus status, String error) {
        super(message, status, error);
    }

    protected ResourceException(String message, HttpStatus status, String error, Throwable cause) {
        super(message, status, error, cause);
    }
}
