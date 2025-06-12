package fun.dashspace.carrentalsystem.security;

import fun.dashspace.carrentalsystem.dto.response.common.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class FallbackController {

    @RequestMapping("/**")
    public ResponseEntity<ErrorResponse> handleNotFound(HttpServletRequest request) {
        var errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("The requested endpoint '" + request.getRequestURI() + "' was not found")
                .error("Not Found")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
