package fun.dashspace.carrentalsystem.auth;

import org.springframework.security.core.userdetails.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenValidator {

    private final JwtTokenProvider jwtTokenProvider;

    public boolean validateTokenWithUserDetails(String token, UserDetails userDetails) {
        try {
            if (!validateToken(token)) {
                return false;
            }

            String username = jwtTokenProvider.getUsernameFromToken(token);
            return username.equals(userDetails.getUsername()) &&
                    jwtTokenProvider.isAccessToken(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }


    public String getUsernameFromToken(String token) {
        return jwtTokenProvider.getUsernameFromToken(token);
    }

    public Integer getUserIdFromToken(String token) {
        return jwtTokenProvider.getUserIdFromToken(token);
    }

    public boolean isRefreshToken(String token) {
        return jwtTokenProvider.isRefreshToken(token);
    }

    public boolean isAccessToken(String token) {
        return jwtTokenProvider.isAccessToken(token);
    }
}