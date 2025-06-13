package fun.dashspace.carrentalsystem.service.auth;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String generateAccessToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);

    boolean validateToken(String token);
    boolean isTokenExpired(String token);
    boolean isRefreshToken(String token);
    boolean isAccessToken(String token);

    String extractUsername(String token);
    Integer extractUserId(String token);
    String extractTokenId(String token);

    long getExpirationTimeFromNow(String token);
}
