package fun.dashspace.carrentalsystem.dto.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import fun.dashspace.carrentalsystem.dto.common.metadata.Metadata;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private Integer status;
    private String message;
    private T data;
    private Metadata metadata;

    public static <T> ApiResponse<T> ok(String message) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return ApiResponse.<T>builder()
                .data(data)
                .status(HttpStatus.OK.value())
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(message)
                .build();
    }
}
