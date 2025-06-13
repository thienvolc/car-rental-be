package fun.dashspace.carrentalsystem.exception.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final String error;

    protected CustomException(String message, HttpStatus status, String error) {
        super(message);
        this.status = status;
        this.error = error;
    }

    protected CustomException(String message, HttpStatus status, String error, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.error = error;
    }
}
