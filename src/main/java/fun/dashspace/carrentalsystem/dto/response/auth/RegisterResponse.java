package fun.dashspace.carrentalsystem.dto.response.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private boolean success;
    private String message;
    private String email;
    private Integer userId;
}
