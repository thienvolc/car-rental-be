package fun.dashspace.carrentalsystem.dto.response.auth;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginResponse {
    private boolean success;
    private String message;
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
    private UserInfo user;
}


