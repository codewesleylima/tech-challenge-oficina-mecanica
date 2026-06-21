package com.safiap.techchallengeoficinamecanica.modules.auth.infrastructure.security;

import com.safiap.techchallengeoficinamecanica.modules.auth.application.service.AuthenticatedUser;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.service.Authenticator;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.AuthException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpringSecurityAuthenticator implements Authenticator {

    private final AuthenticationManager authenticationManager;

    public SpringSecurityAuthenticator(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthenticatedUser authenticate(String email, String rawPassword) {
        Authentication auth;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, rawPassword)
            );
        } catch (AuthenticationException e) {
            throw new AuthException(e.getMessage());
        }

        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.substring("ROLE_".length()))
                .toList();

        return new AuthenticatedUser(auth.getName(), roles);
    }
}
