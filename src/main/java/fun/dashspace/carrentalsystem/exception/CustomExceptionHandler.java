package fun.dashspace.carrentalsystem.exception;

import fun.dashspace.carrentalsystem.dto.response.common.ErrorResponse;
import fun.dashspace.carrentalsystem.exception.custom.CustomException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, WebRequest req) {
        return buildErrorResponse(ex.getMessage(), ex.getStatus(), ex.getError());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status, String error) {
        var errorRes = ErrorResponse.builder()
                .success(false)
                .status(status.value())
                .message(message)
                .error(error)
                .build();
        return new ResponseEntity<>(errorRes, status);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return buildErrorResponse(
                "The requested resource was not found: " + ex.getRequestURL(),
                HttpStatus.NOT_FOUND,
                "Not Found"
        );
    }

    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, "Authentication Failed");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return buildErrorResponse("Access denied: " + ex.getMessage(), HttpStatus.FORBIDDEN, "Forbidden");
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex) {
        return buildErrorResponse("JWT error: " + ex.getMessage(), HttpStatus.UNAUTHORIZED, "Invalid Token");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return buildErrorResponse(
                "User not found: " + ex.getMessage(),
                HttpStatus.NOT_FOUND,
                "User Not Found"
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        return buildErrorResponse(
                "An unexpected error occurred: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error"
        );
    }
}
