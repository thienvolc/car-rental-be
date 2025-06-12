package fun.dashspace.carrentalsystem.dto.request.auth;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateUserSessionRequest {
    private Integer userId;
    private String refreshTokenId;
    private String deviceInfo;
    private String ipAddress;
    private String userAgent;
    private long expirationTime;
}
