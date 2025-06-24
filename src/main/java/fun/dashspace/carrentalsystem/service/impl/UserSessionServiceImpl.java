package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.dto.auth.request.CreateUserSessionRequest;
import fun.dashspace.carrentalsystem.entity.User;
import fun.dashspace.carrentalsystem.entity.UserSession;
import fun.dashspace.carrentalsystem.exception.custom.auth.TokenException;
import fun.dashspace.carrentalsystem.exception.custom.auth.UserSessionExpiredException;
import fun.dashspace.carrentalsystem.exception.custom.resource.ResourceNotFoundException;
import fun.dashspace.carrentalsystem.repository.UserRepo;
import fun.dashspace.carrentalsystem.repository.UserSessionRepo;
import fun.dashspace.carrentalsystem.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSessionServiceImpl implements UserSessionService {

    private final UserSessionRepo userSessionRepo;
    private final UserRepo userRepo;

    private final long EXPIRATION_CLEANUP_INTERVAL = 10 * 24 * 60 * 60 * 1000;

    @Override
    public void createSession(CreateUserSessionRequest req) {
        var user = getUserByIdOrThrow(req.getUserId());
        removeSessionByDeviceIfExists(req, user);
        var userSession = buildUserSession(req, user);
        userSessionRepo.save(userSession);
    }

    private void removeSessionByDeviceIfExists(CreateUserSessionRequest req, User user) {
        getSessionByDevice(req, user)
                .ifPresent(userSessionRepo::delete);
    }

    private Optional<UserSession> getSessionByDevice(CreateUserSessionRequest req, User user) {
        return userSessionRepo.findByUserIdAndIpAddressAndUserAgentAndDeviceInfo(
                user.getId(), req.getIpAddress(), req.getUserAgent(), req.getDeviceInfo());
    }

    private User getUserByIdOrThrow(Integer userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new TokenException("User not found"));
    }

    private UserSession buildUserSession(CreateUserSessionRequest req, User user) {
        return UserSession.builder()
                .user(user)
                .refreshTokenId(req.getRefreshTokenId())
                .userAgent(req.getUserAgent())
                .deviceInfo(req.getDeviceInfo())
                .loginTime(Instant.now())
                .expiredAt(req.getExpiryTime())
                .build();
    }

    @Override
    public void refreshUserSession(Integer userId, String oldRefreshTokenId, String newFreshTokenId) {
        var session = getSessionOrThrow(userId, oldRefreshTokenId);
        verifySessionNotExpired(session);
        session.setRefreshTokenId(newFreshTokenId);
        userSessionRepo.save(session);
    }

    private UserSession getSessionOrThrow(Integer userId, String oldRefreshTokenId) {
        return userSessionRepo.findByUserIdAndRefreshTokenId(userId, oldRefreshTokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found for user with ID: " + userId));
    }


    private void verifySessionNotExpired(UserSession session) {
        if (session.isExpired())
            throw new UserSessionExpiredException("User session has expired");
    }

    @Override
    public void invalidateSession(Integer userId, String refreshTokenId) {
        userSessionRepo.deleteByUserIdAndRefreshTokenId(userId, refreshTokenId);
    }

    @Override
    public void invalidateAllSessions(Integer userId) {
        userSessionRepo.deleteAllByUserId(userId);
    }

    @Override
    @Transactional
    @Scheduled(fixedRate = EXPIRATION_CLEANUP_INTERVAL)
    public void cleanupExpiredSessions() {
        userSessionRepo.deleteAllByExpiredAtAfter(Instant.now());
    }
}
