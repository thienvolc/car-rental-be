package fun.dashspace.carrentalsystem.dto.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String message;
    private Integer status;
    private String error;

    @Builder.Default
    private Instant timestamp = Instant.now();
}