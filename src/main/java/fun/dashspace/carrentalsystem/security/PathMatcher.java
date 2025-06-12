package fun.dashspace.carrentalsystem.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.List;

@Component
public class PathMatcher {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final List<String> PUBLIC_PATHS = Arrays.asList(
            "/auth/**",
            "/cars/search"
    );

    public boolean isPublicPath(HttpServletRequest request) {
        String requestPath = request.getServletPath();
        return PUBLIC_PATHS.stream()
                .anyMatch(pattern -> antPathMatcher.match(pattern, requestPath));
    }
}