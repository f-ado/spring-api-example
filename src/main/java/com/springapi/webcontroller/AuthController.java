package com.springapi.webcontroller;

import com.springapi.authentication.JwtHandler;
import com.springapi.domain.User;
import com.springapi.repository.RoleRepository;
import com.springapi.repository.UserRepository;
import com.springapi.service.UserService;
import com.springapi.service.request.LoginRequest;
import com.springapi.service.request.SignupRequest;
import com.springapi.service.response.ApiResponse;
import com.springapi.service.response.JwtAuthenticationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtHandler tokenProvider;
    private UserService userService;

    public AuthController(
            final AuthenticationManager authenticationManager,
            final UserRepository userRepository,
            final RoleRepository roleRepository,
            final PasswordEncoder passwordEncoder,
            final JwtHandler tokenProvider,
            final UserService userService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody final LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateJWT(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> processRegistrationForm(@RequestBody final SignupRequest signupRequest,
                                                               final HttpServletRequest request) throws Exception {

        String userCheckResult = userService.checkIfUserRegistered(
                signupRequest.getEmail(),
                signupRequest.getUsername()
        );

        if (userCheckResult != null) {
            return new ResponseEntity<>(new ApiResponse(false, userCheckResult), HttpStatus.OK);
        }

        User user = userService.registerAndSendEmail(signupRequest, request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/")
                .buildAndExpand(user.getUsername()).toUri();
        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    @GetMapping("/confirm")
    public ResponseEntity emailConfirmation(@RequestParam(value = "token") final String token) {
        User user = userService.findByConfirmationToken(token);
        if (user != null) {
            userService.activateUser(user.getId());
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }
}
