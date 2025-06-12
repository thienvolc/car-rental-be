package fun.dashspace.carrentalsystem.service.auth;

import fun.dashspace.carrentalsystem.dto.request.auth.CreateUserSessionRequest;
import fun.dashspace.carrentalsystem.dto.request.auth.RefreshTokenRequest;
import fun.dashspace.carrentalsystem.entity.User;
import fun.dashspace.carrentalsystem.entity.UserSession;
import fun.dashspace.carrentalsystem.exception.custom.JwtTokenException;
import fun.dashspace.carrentalsystem.repository.UserRepository;
import fun.dashspace.carrentalsystem.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSessionServiceImpl implements UserSessionService {

    private final UserSessionRepository userSessionRepository;
    private final UserRepository userRepository;

    @Override
    public UserSession createSession(CreateUserSessionRequest req) {
        try {
            User user = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new JwtTokenException("User not found"));

            Duration duration = Duration.ofSeconds(req.getExpirationTime());
            LocalDateTime expiredAt = LocalDateTime.now().plus(duration);
            UserSession userSession = UserSession.builder()
                    .user(user)
                    .refreshTokenId(req.getRefreshTokenId())
                    .userAgent(req.getUserAgent())
                    .deviceInfo(req.getDeviceInfo())
                    .loginTime(LocalDateTime.now())
                    .expiredAt(expiredAt)
                    .build();

            return userSessionRepository.save(userSession);
        } catch (Exception e) {
            throw new JwtTokenException("Failed to create user session", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserSession validateRefreshToken(String tokenId) {
        var session = userSessionRepository.findByRefreshTokenId(tokenId)
                .orElseThrow(() -> new JwtTokenException("Invalid refresh token"));

        if (isExpiredSession(session)) {
            deleteSession(tokenId);
            throw  new JwtTokenException("Refresh token has expired");
        }

        return session;
    }

    private boolean isExpiredSession(UserSession session) {
        return session.getExpiredAt().isBefore(LocalDateTime.now());
    }

    @Override
    public void deleteSession(String tokenId) {
        try {
            userSessionRepository.deleteByRefreshTokenId(tokenId);
        } catch (Exception e) {
            throw new JwtTokenException("Failed to delete user session", e);
        }
    }

    @Override
    public void deleteAllSession(Integer userId) {
        try {
            userSessionRepository.deleteAllByUserId(userId);
        } catch (Exception e) {
            throw new JwtTokenException("Failed to delete all user sessions", e);
        }
    }

    @Override
    public void cleanupExpiredTokens() {
        try {
            userSessionRepository.deleteByExpiredAtBefore(LocalDateTime.now());
        } catch (Exception e) {
            throw new JwtTokenException("Failed to cleanup expired tokens", e);
        }
    }

    @Override
    public void updateRefreshTokenSession(String tokenId, String newTokenId) {
        try {
            UserSession session = validateRefreshToken(tokenId);
            session.setRefreshTokenId(newTokenId);

            userSessionRepository.save(session);
        } catch (Exception e) {
            throw new JwtTokenException("Failed to update refresh token session", e);
        }
    }
}
