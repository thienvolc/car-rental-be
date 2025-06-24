package fun.dashspace.carrentalsystem.dto.auth.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
public class RefreshTokenResponse {
    private String accessToken;
    private String refreshToken;

    private Integer userId;
    private String userName;
    private String email;

    private Instant expiryTime;

    private List<String> roles;
}
