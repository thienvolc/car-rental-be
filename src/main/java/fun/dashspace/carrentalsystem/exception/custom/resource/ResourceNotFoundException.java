package fun.dashspace.carrentalsystem.exception.custom.resource;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ResourceException {

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String ERROR = "Resource Not Found";

    public ResourceNotFoundException(String message) {
        super(message, STATUS, ERROR);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, STATUS, ERROR, cause);
    }
}
