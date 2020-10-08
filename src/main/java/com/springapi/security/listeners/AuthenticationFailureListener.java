package com.springapi.security.listeners;

import com.springapi.domain.User;
import com.springapi.repository.LoginFailureRepository;
import com.springapi.security.domain.LoginFailure;
import com.springapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationFailureListener {

    private final LoginFailureRepository loginFailureRepository;
    private final UserService userService;

    @EventListener
    public void listen(AuthenticationFailureBadCredentialsEvent event) {
        log.error("Login failed!");
        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();
            log.error("Failed login with username: " + token.getPrincipal());

            LoginFailure.LoginFailureBuilder loginFailureBuilder = LoginFailure.builder();
            loginFailureBuilder.username(token.getPrincipal().toString());

            User user = userService.findByUsername(token.getPrincipal().toString());
            if (user != null) {
                loginFailureBuilder.user(user);
            }

            LoginFailure loginFailure = loginFailureRepository.save(loginFailureBuilder.build());
            log.info("Login failure saved! ID: " + loginFailure.getId());
        }
    }
}
