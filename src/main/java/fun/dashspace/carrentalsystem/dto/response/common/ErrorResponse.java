package fun.dashspace.carrentalsystem.dto.response.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private boolean success;
    private int status;
    private String message;
    private String error;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    public ErrorResponse() {
        this(false, 500, "Internal Server Error", "Error", LocalDateTime.now());
    }

    public ErrorResponse(boolean success, int status, String message, String error, LocalDateTime timestamp) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.error = error;
        this.timestamp = timestamp;
    }
}