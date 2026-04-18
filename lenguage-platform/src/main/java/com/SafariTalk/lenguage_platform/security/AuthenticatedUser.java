package com.SafariTalk.lenguage_platform.security;

import com.SafariTalk.lenguage_platform.model.UserAccountEntity;
import com.SafariTalk.lenguage_platform.model.UserRole;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class AuthenticatedUser implements UserDetails {

    private final Long id;
    private final String email;
    private final String passwordHash;
    private final boolean enabled;
    private final UserRole role;

    public AuthenticatedUser(Long id, String email, String passwordHash, boolean enabled, UserRole role) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.enabled = enabled;
        this.role = role;
    }

    public static AuthenticatedUser from(UserAccountEntity user) {
        return new AuthenticatedUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                user.getRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
