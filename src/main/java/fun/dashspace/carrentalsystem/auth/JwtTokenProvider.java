package fun.dashspace.carrentalsystem.auth;

import fun.dashspace.carrentalsystem.config.properties.JwtProperties;
import fun.dashspace.carrentalsystem.security.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import java.time.Duration;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final Duration accessTokenDuration;
    private final Duration refreshTokenDuration;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
        this.accessTokenDuration = Duration.ofSeconds(jwtProperties.getExpiration());
        this.refreshTokenDuration = Duration.ofSeconds(jwtProperties.getRefreshExpiration());
    }

    public String generateAccessToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return generateAccessToken(userDetails);
    }

    public String generateAccessToken(CustomUserDetails userDetails) {
        Instant now = Instant.now();
        Instant expiration = now.plus(accessTokenDuration);
        List<String> roles = getRolesFromUserDetails(userDetails);

        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .claim("userId", userDetails.getId())
                .claim("roles", roles)
                .claim("tokenType", "access")
                .claim("jti", UUID.randomUUID().toString())
                .signWith(secretKey)
                .compact();
    }

    public List<String> getRolesFromUserDetails(CustomUserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    public String generateRefreshToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return generateRefreshToken(userDetails);
    }

    public String generateRefreshToken(CustomUserDetails userDetails) {
        Instant now = Instant.now();
        Instant expiration = now.plus(refreshTokenDuration);

        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .claim("userId", userDetails.getId())
                .claim("tokenType", "refresh")
                .claim("jti", UUID.randomUUID().toString())
                .signWith(secretKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public String getTokenIdFromToken(String token) {
        return getClaimsFromToken(token).get("jti", String.class);
    }

    public Integer getUserIdFromToken(String token) {
        return getClaimsFromToken(token).get("userId", Integer.class);
    }

    public Claims getClaimsFromToken(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isRefreshToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return "refresh".equals(claims.get("tokenType"));
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isAccessToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return "access".equals(claims.get("tokenType"));
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    public long getExpirationTimeFromNow(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return Duration.between(Instant.now(), expiration.toInstant()).getSeconds();
    }
}