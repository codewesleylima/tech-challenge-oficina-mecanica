package com.safiap.techchallengeoficinamecanica.modules.auth.infrastructure.persistence.implementations;

import com.safiap.techchallengeoficinamecanica.modules.auth.infrastructure.persistence.entities.JpaUserEntity;
import com.safiap.techchallengeoficinamecanica.modules.auth.infrastructure.persistence.repositories.JpaUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final JpaUserRepository userRepository;

    public UserDetailsServiceImpl(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JpaUserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }
}
