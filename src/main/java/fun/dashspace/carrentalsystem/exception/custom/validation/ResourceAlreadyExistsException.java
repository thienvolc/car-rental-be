package fun.dashspace.carrentalsystem.exception.custom.validation;

public class ResourceAlreadyExistsException extends BadRequestException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
