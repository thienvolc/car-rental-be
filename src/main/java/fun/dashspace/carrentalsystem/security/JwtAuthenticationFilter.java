package fun.dashspace.carrentalsystem.security;

import fun.dashspace.carrentalsystem.auth.JwtTokenExtractor;
import fun.dashspace.carrentalsystem.auth.JwtTokenValidator;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenExtractor tokenExtractor;
    private final JwtTokenValidator jwtTokenValidator;
    private final UserDetailsService userDetailsService;
    private final PathMatcher pathMatcher;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            if (!shouldSkipAuthentication(request)) {
                authenticateRequest(request);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldSkipAuthentication(HttpServletRequest request) {
        return pathMatcher.isPublicPath(request) || isAlreadyAuthenticated();
    }

    private boolean isAlreadyAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal());
    }

    private void authenticateRequest(HttpServletRequest request) {
        Optional<String> token = tokenExtractor.extractToken(request);

        if (token.isEmpty()) {
            return;
        }

        if (jwtTokenValidator.validateToken(token.get())) {
            return;
        }

        if (!jwtTokenValidator.isAccessToken(token.get())) {
            return;
        }

        authenticateWithToken(token.get(), request);
    }

    private void authenticateWithToken(String token, HttpServletRequest request) {
        try {
            String username = jwtTokenValidator.getUsernameFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtTokenValidator.validateTokenWithUserDetails(token, userDetails)) {
                setAuthentication(userDetails, request);
            }
        } catch (Exception e) {
            throw new JwtException("Failed to authenticate token", e);
        }
    }

    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}