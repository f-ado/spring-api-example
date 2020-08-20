package com.springapi.authentication;

import com.springapi.domain.User;
import com.springapi.repository.UserRepository;
import com.springapi.service.exception.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                        User.class.getCanonicalName(),
                        "Username or Email",
                        usernameOrEmail
                    )
                );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(final UUID id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(
                    () -> new ResourceNotFoundException(User.class.getCanonicalName(), "UUID", id)
                );
        return UserPrincipal.create(user);
    }
}
