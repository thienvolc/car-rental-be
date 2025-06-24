package fun.dashspace.carrentalsystem.exception.custom.auth;

import org.springframework.http.HttpStatus;

public class InvalidOtpException extends AuthException {
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String ERROR = "Invalid OTP";

    public InvalidOtpException(String message) {
        super(message, STATUS, ERROR);
    }

    public InvalidOtpException(String message, Throwable cause) {
        super(message, STATUS, ERROR, cause);
    }
}