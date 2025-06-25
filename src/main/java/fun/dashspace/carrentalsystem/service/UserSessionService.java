package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.auth.request.CreateUserSessionRequest;

public interface UserSessionService {

    void createSession(CreateUserSessionRequest req);

    void refreshUserSession(Integer userId, String oldRefreshTokenId, String newFreshTokenId);

    void invalidateSession(Integer userId, String refreshTokenId);
    void invalidateAllSessions(Integer userId);

    void cleanupExpiredSessions();
}
