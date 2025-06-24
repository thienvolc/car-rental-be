package fun.dashspace.carrentalsystem.dto.auth.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class CreateUserSessionRequest {
    private Integer userId;
    private String refreshTokenId;
    private String deviceInfo;
    private String ipAddress;
    private String userAgent;
    private Instant expiryTime;
}
