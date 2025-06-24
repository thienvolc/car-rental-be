package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.config.props.JwtProps;
import fun.dashspace.carrentalsystem.exception.custom.auth.TokenException;
import fun.dashspace.carrentalsystem.security.CustomUserDetails;
import fun.dashspace.carrentalsystem.service.JwtService;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Getter
enum TokenType {
    ACCESS, REFRESH
}

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtProps jwtProps;

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken((CustomUserDetails) userDetails, TokenType.ACCESS, jwtProps.getTokenExpiry());
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken((CustomUserDetails) userDetails, TokenType.REFRESH, jwtProps.getRefreshTokenExpiry());
    }

    private String generateToken(CustomUserDetails userDetails, TokenType type, Duration duration) {
        Map<String, Object> claims = buildClaims(userDetails, type);
        return createJwtToken(userDetails.getUsername(), claims, duration);
    }

    private Map<String, Object> buildClaims(CustomUserDetails userDetails, TokenType type) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getId());
        claims.put("tokenType", type);
        claims.put("jti", UUID.randomUUID().toString());

        if (type == TokenType.ACCESS)
            claims.put("roles", extractRoles(userDetails));

        return claims;
    }

    private List<String> extractRoles(CustomUserDetails user) {
        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    private String createJwtToken(String subject, Map<String, Object> claims, Duration tokenDuration) {
        var now = Instant.now();
        var expiration = now.plus(tokenDuration);

        return Jwts.builder()
                .header().type("JWT").and()
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .claims(claims)
                .signWith(jwtProps.getKey())
                .compact();
    }

    @Override
    public void verifyAccessToken(String token) throws TokenException {
        verifyToken(token, TokenType.ACCESS);
    }

    @Override
    public void verifyRefreshToken(String token) throws TokenException {
        verifyToken(token, TokenType.REFRESH);
    }

    private void verifyToken(String token, TokenType type) throws TokenException {
        if (isTokenExpired(token))
            throw new TokenException("Token has expired");

        if (!type.equals(getTokenType(token)))
            throw new TokenException("Invalid token type");
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

    private Claims getClaims(String token) {
        return parseToken(token).getPayload();
    }

    private Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(jwtProps.getKey())
                .build()
                .parseSignedClaims(token);
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

    private TokenType getTokenType(String token) {
        return getClaims(token).get("tokenType", TokenType.class);
    }

    @Override
    public Instant getExpiryTime(String token) {
        return getClaims(token).getExpiration().toInstant();
    }

    @Override
    public Duration getRemainingTime(String token) {
        return Duration.between(Instant.now(), getExpiryTime(token));
    }
}
