package com.SafariTalk.lenguage_platform.security;

import com.SafariTalk.lenguage_platform.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        String email = username.trim().toLowerCase();
        return userAccountRepository
                .findByEmailIgnoreCase(email)
                .map(user -> {
                    if (!user.isEnabled()) {
                        throw new DisabledException("Account is disabled");
                    }
                    return AuthenticatedUser.from(user);
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
