package com.springapi.security.listeners;

import com.springapi.domain.User;
import com.springapi.repository.LoginFailureRepository;
import com.springapi.repository.UserRepository;
import com.springapi.security.domain.LoginFailure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationFailureListener {

    private final LoginFailureRepository loginFailureRepository;
    private final UserRepository userRepository;

    @EventListener
    public void listen(AuthenticationFailureBadCredentialsEvent event) {
        log.error("Login failed!");
        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();
            log.error("Failed login with username: " + token.getPrincipal());

            LoginFailure.LoginFailureBuilder loginFailureBuilder = LoginFailure.builder();
            loginFailureBuilder.username(token.getPrincipal().toString());

            Optional<User> user = userRepository.findByUsername(token.getPrincipal().toString());
            user.ifPresent(loginFailureBuilder::user);

            LoginFailure loginFailure = loginFailureRepository.save(loginFailureBuilder.build());
            log.info("Login failure saved! ID: " + loginFailure.getId());

            if (loginFailure.getUser() != null) {
                lockAccount(loginFailure.getUser());
            }
        }
    }

    private void lockAccount(User user) {
        List<LoginFailure> loginFailureList = loginFailureRepository.findAllByUserAndCreatedDateIsAfter(
            user,
            Timestamp.valueOf(LocalDateTime.now().minusDays(1))
        );

        if (loginFailureList.size() > 4) {
            log.info("Locking the account " + user.getId());
            user.setAccountNonLocked(false);
            userRepository.save(user);
        }
    }
}
