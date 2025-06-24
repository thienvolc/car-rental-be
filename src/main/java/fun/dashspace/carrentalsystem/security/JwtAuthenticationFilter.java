package fun.dashspace.carrentalsystem.security;

import fun.dashspace.carrentalsystem.exception.custom.auth.TokenException;
import fun.dashspace.carrentalsystem.exception.custom.auth.UnauthorizedException;
import fun.dashspace.carrentalsystem.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest req,
            @NonNull HttpServletResponse res,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (!isPublicPath(req)) {
            String accessToken = extractToken(req);
            System.out.println("Access Token: " + accessToken);
            verifyToken(accessToken);
            authenticateUserByToken(accessToken, req);
        }
        filterChain.doFilter(req, res);
    }

    private boolean isPublicPath(HttpServletRequest req) {
        String path = req.getServletPath();
        return Arrays.stream(SecurityPaths.PUBLIC_PATHS)
                .anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }

    private String extractToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer "))
            return header.substring(7);

        throw new UnauthorizedException("Missing token");
    }

    private void verifyToken(String token) {
        try {
            jwtService.verifyAccessToken(token);
        } catch (TokenException ex) {
            throw new UnauthorizedException(ex.toString());
        }
    }

    private void authenticateUserByToken(String accessToken, HttpServletRequest req) {
        var username = jwtService.extractUsername(accessToken);
        var userDetails = userDetailsService.loadUserByUsername(username);

        var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}