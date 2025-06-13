package fun.dashspace.carrentalsystem.service.auth;

import fun.dashspace.carrentalsystem.dto.request.auth.CreateUserSessionRequest;
import fun.dashspace.carrentalsystem.entity.UserSession;

public interface UserSessionService {

    UserSession createSession(CreateUserSessionRequest req);
    void deleteSession(String tokenId);
    void deleteAllSessions(Integer userId);

    UserSession validateRefreshToken(String tokenId);
    void updateRefreshTokenId(String oldTokenId, String newTokenId);

    void cleanupExpiredSessions();
}
