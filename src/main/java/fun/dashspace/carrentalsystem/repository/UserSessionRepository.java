package fun.dashspace.carrentalsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fun.dashspace.carrentalsystem.entity.UserSession;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Integer> {

    Optional<UserSession> findByRefreshTokenId(String refreshTokenId);

    @Modifying
    @Query("DELETE FROM UserSession s WHERE s.refreshTokenId = :tokenId")
    int deleteByRefreshTokenId(@Param("tokenId") String tokenId);

    @Modifying
    @Query("DELETE FROM UserSession s WHERE s.user.id = :userId")
    int deleteAllByUserId(@Param("userId") Integer userId);

    @Modifying
    @Query("DELETE FROM UserSession s WHERE s.expiredAt < :expiredTime")
    int deleteByExpiredAtBefore(@Param("expiredTime") LocalDateTime expiredTime);

    // Additional useful queries
    @Query("SELECT COUNT(s) FROM UserSession s WHERE s.user.id = :userId")
    long countActiveSessionsByUserId(@Param("userId") Integer userId);

    @Query("SELECT s FROM UserSession s WHERE s.user.id = :userId ORDER BY s.loginTime DESC")
    List<UserSession> findAllByUserIdOrderByLoginTimeDesc(@Param("userId") Integer userId);
}