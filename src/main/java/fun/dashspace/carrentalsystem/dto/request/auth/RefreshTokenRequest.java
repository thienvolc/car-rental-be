package fun.dashspace.carrentalsystem.dto.request.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RefreshTokenRequest {
    private String refreshToken;
    private Integer userId;
    private String tokenId;
    private String deviceInfo;
    private String ipAddress;
    private String userAgent;
}
