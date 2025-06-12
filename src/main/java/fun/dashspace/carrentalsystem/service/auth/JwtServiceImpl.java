package fun.dashspace.carrentalsystem.service.auth;

import fun.dashspace.carrentalsystem.auth.JwtTokenProvider;
import fun.dashspace.carrentalsystem.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String generateAccessToken(CustomUserDetails userDetails) {
        return jwtTokenProvider.generateAccessToken(userDetails);
    }

    @Override
    public String generateRefreshToken(CustomUserDetails userDetails) {
        return jwtTokenProvider.generateRefreshToken(userDetails);
    }

    @Override
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    @Override
    public String extractUsername(String token) {
        return jwtTokenProvider.getUsernameFromToken(token);
    }

    @Override
    public Integer extractUserId(String token) {
        return jwtTokenProvider.getUserIdFromToken(token);
    }

    @Override
    public String extractTokenId(String token) {
        return jwtTokenProvider.getTokenIdFromToken(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return jwtTokenProvider.isTokenExpired(token);
    }

    @Override
    public boolean isRefreshToken(String token) {
        return jwtTokenProvider.isRefreshToken(token);
    }

    @Override
    public boolean isAccessToken(String token) {
        return jwtTokenProvider.isAccessToken(token);
    }

    @Override
    public long getExpirationTimeFromNow(String token) {
        return jwtTokenProvider.getExpirationTimeFromNow(token);
    }
}
