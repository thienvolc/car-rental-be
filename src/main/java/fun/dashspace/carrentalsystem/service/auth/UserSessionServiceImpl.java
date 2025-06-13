package fun.dashspace.carrentalsystem.service.auth;

import fun.dashspace.carrentalsystem.dto.request.auth.CreateUserSessionRequest;
import fun.dashspace.carrentalsystem.entity.User;
import fun.dashspace.carrentalsystem.entity.UserSession;
import fun.dashspace.carrentalsystem.exception.custom.auth.TokenException;
import fun.dashspace.carrentalsystem.repository.UserRepository;
import fun.dashspace.carrentalsystem.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSessionServiceImpl implements UserSessionService {

    private final UserSessionRepository userSessionRepository;
    private final UserRepository userRepository;

    @Override
    public UserSession createSession(CreateUserSessionRequest req) {
        var user = findUserById(req.getUserId());
        var userSession = buildUserSession(req, user);
        return userSessionRepository.save(userSession);
    }

    private User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new TokenException("User not found"));
    }

    private UserSession buildUserSession(CreateUserSessionRequest req, User user) {
        return UserSession.builder()
                .user(user)
                .refreshTokenId(req.getRefreshTokenId())
                .userAgent(req.getUserAgent())
                .deviceInfo(req.getDeviceInfo())
                .loginTime(LocalDateTime.now())
                .expiredAt(calculateExpirationTime(req.getExpirationTime()))
                .build();
    }

    private LocalDateTime calculateExpirationTime(long expirationTimeSeconds) {
        return LocalDateTime.now().plusSeconds(expirationTimeSeconds);
    }

    @Override
    @Transactional(readOnly = true)
    public UserSession validateRefreshToken(String tokenId) {
        var userSession = findSessionByTokenId(tokenId);
        validateSessionNotExpired(userSession);
        return userSession;
    }

    private UserSession findSessionByTokenId(String tokenId) {
        return userSessionRepository.findByRefreshTokenId(tokenId)
                .orElseThrow(() -> new TokenException("Invalid refresh token"));
    }

    private void validateSessionNotExpired(UserSession userSession) {
        if (isExpiredSession(userSession)) {
            deleteSession(userSession.getRefreshTokenId());
            throw new TokenException("Refresh token has expired");
        }
    }

    private boolean isExpiredSession(UserSession session) {
        return session.getExpiredAt().isBefore(LocalDateTime.now());
    }

    @Override
    public void deleteSession(String tokenId) {
        userSessionRepository.deleteByRefreshTokenId(tokenId);
    }

    @Override
    public void deleteAllSessions(Integer userId) {
        userSessionRepository.deleteAllByUserId(userId);
    }

    @Override
    public void updateRefreshTokenId(String tokenId, String newTokenId) {
        UserSession session = findSessionByTokenId(tokenId);
        session.setRefreshTokenId(newTokenId);
        userSessionRepository.save(session);
    }

    @Override
    public void cleanupExpiredSessions() {
        userSessionRepository.deleteByExpiredAtBefore(LocalDateTime.now());
    }
}
