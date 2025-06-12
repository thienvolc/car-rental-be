package fun.dashspace.carrentalsystem.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
public class JwtTokenExtractor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    public Optional<String> extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        return extractTokenFromAuthHeader(bearerToken);
    }

    public Optional<String> extractTokenFromAuthHeader(String authHeader) {
        return isValidAuthHeader(authHeader)
                ? Optional.of(authHeader.substring(BEARER_PREFIX.length()))
                : Optional.empty();
    }

    public boolean isValidAuthHeader(String authHeader) {
        return StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_PREFIX);
    }
}