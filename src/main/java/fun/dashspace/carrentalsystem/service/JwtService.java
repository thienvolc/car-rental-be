package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.exception.custom.auth.TokenException;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.time.Instant;

public interface JwtService {
    String generateAccessToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);

    boolean isTokenExpired(String token);
    void verifyAccessToken(String token) throws TokenException;
    void verifyRefreshToken(String token) throws TokenException;

    String extractUsername(String token);
    Integer extractUserId(String token);
    String extractTokenId(String token);

    Duration getRemainingTime(String token);
    Instant getExpiryTime(String token);
}
