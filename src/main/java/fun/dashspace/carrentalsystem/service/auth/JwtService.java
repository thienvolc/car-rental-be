package fun.dashspace.carrentalsystem.service.auth;

import fun.dashspace.carrentalsystem.security.CustomUserDetails;

public interface JwtService {
    String generateAccessToken(CustomUserDetails userDetails);

    String generateRefreshToken(CustomUserDetails userDetails);

    boolean validateToken(String token);

    String extractUsername(String token);

    Integer extractUserId(String token);

    String extractTokenId(String token);

    boolean isTokenExpired(String token);

    boolean isRefreshToken(String token);

    boolean isAccessToken(String token);

    long getExpirationTimeFromNow(String token);
}
