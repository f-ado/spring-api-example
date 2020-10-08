package com.springapi.security.listeners;

import com.springapi.authentication.UserPrincipal;
import com.springapi.security.domain.LoginSuccess;
import com.springapi.domain.User;
import com.springapi.repository.LoginSuccessRepository;
import com.springapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationSuccessListener {

    private final LoginSuccessRepository loginRepository;
    private final UserService userService;

    @EventListener
    public void listen(AuthenticationSuccessEvent event) {
        log.info("New login!");
        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            final LoginSuccess.LoginSuccessBuilder loginBuilder = LoginSuccess.builder();
            final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();
            if (token.getPrincipal() instanceof UserPrincipal) {
                final UserPrincipal userPrincipal = (UserPrincipal) token.getPrincipal();
                final User user = userService.findOne(userPrincipal.getId());
                loginBuilder.user(user);
                log.info("User logged: " + userPrincipal.getUsername());
            }

            final LoginSuccess login = loginRepository.save(loginBuilder.build());
            log.info("Login saved. ID: " + login.getId());
        }
    }
}
