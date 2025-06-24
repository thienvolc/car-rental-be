package fun.dashspace.carrentalsystem.exception.custom.validation;

public class EmailNotVerifiedException extends BadRequestException {
    public EmailNotVerifiedException(String email) {
        super("Email not verified: " + email);
    }

    public EmailNotVerifiedException(String email, Throwable cause) {
        super("Email not verified: " + email, cause);
    }
}
