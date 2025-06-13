package fun.dashspace.carrentalsystem.exception.custom.resource;

import org.springframework.http.HttpStatus;

public class FileUploadFailedException extends ResourceException {

    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final String ERROR = "Upload Failed";

    public FileUploadFailedException(String message) {
        super(message, STATUS, ERROR);
    }

    public FileUploadFailedException(String message, Throwable cause) {
        super(message, STATUS, ERROR, cause);
    }
}
