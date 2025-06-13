package fun.dashspace.carrentalsystem.exception.custom.validation;

public class EmailAlreadyExistsException extends BadRequestException {
    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email);
    }

    public EmailAlreadyExistsException(String email, Throwable cause) {
        super("Email already exists: " + email, cause);
    }
}
