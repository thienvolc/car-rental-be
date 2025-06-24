package fun.dashspace.carrentalsystem.exception.custom.auth;

public class UserSessionExpiredException extends UnauthorizedException {
    public UserSessionExpiredException(String message) {
        super(message);
    }

    public UserSessionExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
