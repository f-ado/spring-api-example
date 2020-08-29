package com.springapi.service;

import com.springapi.authentication.UserPrincipal;
import com.springapi.security.domain.Role;
import com.springapi.security.domain.RoleName;
import com.springapi.domain.User;
import com.springapi.filters.specifications.user.UserSpecification;
import com.springapi.filters.user.UserFilter;
import com.springapi.jms.messages.UserRegisteredMessage;
import com.springapi.jms.producers.UserRegisteredProducer;
import com.springapi.repository.RoleRepository;
import com.springapi.repository.UserRepository;
import com.springapi.service.dto.CurrentUserDto;
import com.springapi.service.dto.UserDto;
import com.springapi.service.exception.AppException;
import com.springapi.service.request.SignupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("userService")
public class UserService {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static final String CONFIRMATION_URL_TEMPLATE = "%s://%s:%s/api/auth";

    private UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private final UserRegisteredProducer userRegisteredProducer;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, EmailService emailService, PasswordEncoder passwordEncoder, UserRegisteredProducer userRegisteredProducer) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.userRegisteredProducer = userRegisteredProducer;
    }

    public UserDto getUser(final UUID id) {
        return new UserDto(userRepository.getOne(id));
    }

    public Page<UserDto> getAllUsers(final Pageable page) {
        PageRequest nonSortablePageRequest = PageRequest.of(page.getPageNumber(), page.getPageSize());
        Page<User> users = userRepository.findAll(nonSortablePageRequest);
        List<User> usersPage = users.getContent();

        return new PageImpl<>(usersPage.stream().map(UserDto::new).collect(Collectors.toList()),
                page, users.getTotalElements());
    }

    public Page<UserDto> getFilteredUsers(final UserFilter filter, final Pageable page) {
        PageRequest nonSortablePageRequest = PageRequest.of(page.getPageNumber(), page.getPageSize());
        Page<User> users = userRepository.findAll(new UserSpecification(filter), nonSortablePageRequest);
        List<User> usersPage = users.getContent();
        return new PageImpl<>(usersPage.stream().map(UserDto::new).collect(Collectors.toList()),
                page, users.getTotalElements());
    }

    public User findByEmail(final String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            return userRepository.findByEmail(email).get();
        } else {
            return null;
        }
    }

    public User findOne(final UUID id) {
        return userRepository.getOne(id);
    }

    public boolean existsByEmail(final String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(final String username) {
        return userRepository.existsByUsername(username);
    }

    public User findByConfirmationToken(final String confirmationToken) {
        if (userRepository.findByConfirmationToken(confirmationToken).isPresent()) {
            return userRepository.findByConfirmationToken(confirmationToken).get();
        } else {
            return null;
        }
    }

    public void activateUser(final UUID uuid) {
        User user = userRepository.findById(uuid).get();
        user.setActive(true);
        saveUser(user);
        LOGGER.info("User {} activated", uuid);
    }

    @Transactional
    public String checkIfUserRegistered(String email, String username) {
        String result = null;
        if (existsByEmail(email)) {
            result = "Email is already taken!";
        }
        if(existsByUsername(username)){
            result = "Username is already taken!";
        }
        return result;
    }

    public User registerAndSendEmail(final SignupRequest signupRequest, final HttpServletRequest request) {
        // Create new user
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role does not exist."));

        user.setRoles(Collections.singleton(userRole));

        // Set user activity to false until confirmation is completed
        user.setActive(false);

        // Generate confirmation link
        user.setConfirmationToken(UUID.randomUUID().toString());

        saveUser(user);
        LOGGER.debug("User={} created.", user.getId());

        String appUrl = String.format(
                CONFIRMATION_URL_TEMPLATE,
                request.getScheme(),
                request.getServerName(),
                request.getLocalPort()
        );

        final UserRegisteredMessage message = new UserRegisteredMessage(UUID.randomUUID(), appUrl, user);
        userRegisteredProducer.sendMessage(message);

        return user;
    }

    private void saveUser(final User user) {
        userRepository.save(user);
    }

    public CurrentUserDto getCurrentUser(final UserPrincipal userPrincipal) {
        User currentUser = findOne(userPrincipal.getId());
        return new CurrentUserDto(currentUser);
    }
}
