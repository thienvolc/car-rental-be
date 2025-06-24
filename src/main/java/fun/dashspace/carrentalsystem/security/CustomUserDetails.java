package fun.dashspace.carrentalsystem.security;

import fun.dashspace.carrentalsystem.entity.Role;
import fun.dashspace.carrentalsystem.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record CustomUserDetails(User user) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoleNames().stream()
                .map(name -> new SimpleGrantedAuthority("ROLE_" + name))
                .toList();
    }

    public List<String> getRoleNames() {
        return user.getRoles().stream()
                .map(Role::getName)
                .map(Enum::name)
                .toList();
    }

    public String getDisplayName() {
        return user.getUsername();
    }

    public Integer getId() {
        return user.getId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isInactive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !user.isBanned();
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}
