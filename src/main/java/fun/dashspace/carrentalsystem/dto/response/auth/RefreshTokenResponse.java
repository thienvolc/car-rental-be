package fun.dashspace.carrentalsystem.dto.response.auth;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RefreshTokenResponse {
    private boolean success;
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long expiresIn;
}
