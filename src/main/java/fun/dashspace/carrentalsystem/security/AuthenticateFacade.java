package fun.dashspace.carrentalsystem.security;

import fun.dashspace.carrentalsystem.entity.User;
import fun.dashspace.carrentalsystem.exception.custom.auth.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticateFacade {
    public CustomUserDetails getCurrentUserDetails() {
        Optional<Authentication> auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
        return auth
                .filter(this::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(CustomUserDetails.class::cast)
                .orElseThrow(() -> new UnauthorizedException("User is not authenticated"));
    }

    public User getCurrentUser() {
        return getCurrentUserDetails().user();
    }

    private boolean isAuthenticated(Authentication auth) {
        return auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser");
    }
}
