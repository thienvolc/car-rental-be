package fun.dashspace.carrentalsystem.service.auth;

import fun.dashspace.carrentalsystem.dto.request.auth.CreateUserSessionRequest;
import fun.dashspace.carrentalsystem.dto.request.auth.RefreshTokenRequest;
import fun.dashspace.carrentalsystem.entity.UserSession;

public interface UserSessionService {

    UserSession createSession(CreateUserSessionRequest req);

    UserSession validateRefreshToken(String tokenId);

    void deleteSession(String tokenId);

    void deleteAllSession(Integer userId);

    void cleanupExpiredTokens();

    void updateRefreshTokenSession(String tokenId, String newTokenId);
}
