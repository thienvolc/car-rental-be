package fun.dashspace.carrentalsystem.repository;

import fun.dashspace.carrentalsystem.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserSessionRepo extends JpaRepository<UserSession, Integer> {
    Optional<UserSession> findByUserIdAndIpAddressAndUserAgentAndDeviceInfo(
            Integer userId, String ipAddress, String userAgent, String deviceInfo);

    Optional<UserSession> findByUserIdAndRefreshTokenId(Integer userId, String refreshTokenId);

    void deleteByUserIdAndRefreshTokenId(Integer userId, String refreshTokenId);
    void deleteAllByUserId(Integer userId);

    void deleteAllByExpiredAtAfter(Instant expiredAt);
}