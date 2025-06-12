package fun.dashspace.carrentalsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fun.dashspace.carrentalsystem.entity.UserSession;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Integer> {
    Optional<UserSession> findByRefreshTokenId(String refreshTokenId);

    int deleteByRefreshTokenId(String refreshTokenId);

    int deleteAllByUserId(Integer userId);

    int deleteByExpiredAtBefore(LocalDateTime now);
}