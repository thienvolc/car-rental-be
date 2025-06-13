package fun.dashspace.carrentalsystem.service.auth;

import fun.dashspace.carrentalsystem.config.properties.JwtProperties;
import fun.dashspace.carrentalsystem.security.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {

    private final SecretKey secretKey;
    private final Duration accessTokenDuration;
    private final Duration refreshTokenDuration;

    public JwtServiceImpl(JwtProperties jwtProperties) {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
        this.accessTokenDuration = Duration.ofSeconds(jwtProperties.getExpiration());
        this.refreshTokenDuration = Duration.ofSeconds(jwtProperties.getRefreshExpiration());
    }

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        var customUser = (CustomUserDetails) userDetails;
        return buildToken(customUser, accessTokenDuration, "access");
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        var customUser = (CustomUserDetails) userDetails;
        return buildToken(customUser, refreshTokenDuration, "refresh");
    }

    private String buildToken(CustomUserDetails user, Duration duration, String tokenType) {
        var now = Instant.now();
        var expiration = now.plus(duration);

        var builder = Jwts.builder()
                .header().type("JWT").and()
                .subject(user.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .claim("userId", user.getId())
                .claim("tokenType", tokenType)
                .claim("jti", UUID.randomUUID().toString());

        if ("access".equals(tokenType)) {
            builder.claim("roles", extractRoles(user));
        }

        return builder.signWith(secretKey).compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    @Override
    public Integer extractUserId(String token) {
        return getClaims(token).get("userId", Integer.class);
    }

    @Override
    public String extractTokenId(String token) {
        return getClaims(token).get("jti", String.class);
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

    @Override
    public boolean isRefreshToken(String token) {
        return "refresh".equals(getTokenType(token));
    }

    @Override
    public boolean isAccessToken(String token) {
        return "access".equals(getTokenType(token));
    }

    @Override
    public long getExpirationTimeFromNow(String token) {
        var expiration = getClaims(token).getExpiration();
        return (expiration.getTime() - System.currentTimeMillis()) / 1000;
    }

    private List<String> extractRoles(CustomUserDetails user) {
        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    private String getTokenType(String token) {
        try {
            return getClaims(token).get("tokenType", String.class);
        } catch (JwtException e) {
            return null;
        }
    }

    private Claims getClaims(String token) {
        return parseToken(token).getPayload();
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
    }
}