package fun.dashspace.carrentalsystem.exception.custom.validation;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ValidationException {

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String ERROR = "Bad Request";

    public BadRequestException(String message) {
        super(message, STATUS, ERROR);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, STATUS, ERROR, cause);
    }
}