package com.springapi.service;

import com.springapi.domain.Gender;
import com.springapi.domain.User;
import com.springapi.filters.specifications.user.UserSpecification;
import com.springapi.filters.user.UserFilter;
import com.springapi.jms.producers.UserRegisteredProducer;
import com.springapi.repository.RoleRepository;
import com.springapi.repository.UserRepository;
import com.springapi.service.EmailService;
import com.springapi.service.UserService;
import com.springapi.service.dto.UserDto;
import org.easymock.Capture;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;

@RunWith(EasyMockRunner.class)
public class UserServiceTest extends EasyMockSupport {
    private final String TEST_EMAIL = "john@test.com";

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRegisteredProducer userRegisteredProducer;

    private Pageable nonSortablePageRequest;

    @Before
    public void setUp() throws Exception {
        this.userService = new UserService(userRepository, roleRepository, emailService, passwordEncoder, userRegisteredProducer);
        nonSortablePageRequest = PageRequest.of(1, 20);
    }

    @Test
    public void testGetUser() {
        UUID id = UUID.randomUUID();
        User user = createUser(id);
        user.setFirstName("FirstName");

        expect(userRepository.getOne(id)).andReturn(user);

        replayAll();

        UserDto result = userService.getUser(id);

        assertEquals(result.getFirstName(), user.getFirstName());

        verifyAll();
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>();

        users.add(createUser(UUID.randomUUID()));
        users.add(createUser(UUID.randomUUID()));
        users.add(createUser(UUID.randomUUID()));

        Page<User> userPageable = new PageImpl(users);

        expect(userRepository.findAll(nonSortablePageRequest))
                .andReturn(userPageable);

        replayAll();

        Page<UserDto> result = userService.getAllUsers(nonSortablePageRequest);

        assertNotNull(result.getContent());

        verifyAll();
    }

    @Test
    public void testGetFilteredUsers() {

        Capture<UserSpecification> userSpecificationCapture = Capture.newInstance();
        Capture<PageRequest> pageRequestCapture = Capture.newInstance();

        final UserFilter filter = UserFilter.builder()
                .active(true)
                .search("Doe")
                .build();

        List<User> filteredUsers = new ArrayList<>();
        filteredUsers.add(createUser(UUID.randomUUID(), true, "John", "Doe", TEST_EMAIL, Gender.M));
        filteredUsers.add(createUser(UUID.randomUUID(), true, "Jane", "Doe", "jane@test.com", Gender.F));

        Page<User> userPageable = new PageImpl(filteredUsers);

        expect(userRepository.findAll(capture(userSpecificationCapture), capture(pageRequestCapture)))
                .andReturn(userPageable);

        replayAll();

        Page<UserDto> result = userService.getFilteredUsers(filter, PageRequest.of(1, 20));

        assertNotNull(result.getContent());
        assertEquals(result.getContent().size(), filteredUsers.size());

        verifyAll();
    }

    @Test
    public void testFindByEmail() {
        final User user = createUser(UUID.randomUUID(), true, "John", "Doe", TEST_EMAIL, Gender.M);

        expect(userRepository.findByEmail(TEST_EMAIL))
            .andReturn(Optional.of(user))
            .times(2);

        replayAll();

        final User result = userService.findByEmail(TEST_EMAIL);

        assertNotNull(result);
        assertEquals(result.getEmail(), TEST_EMAIL);

        verifyAll();
    }

    @Test
    public void testFindOne() {
        final UUID id = UUID.randomUUID();
        final User user = createUser(id, true, "John", "Doe", TEST_EMAIL, Gender.M);

        expect(userRepository.getOne(id))
                .andReturn(user);

        replayAll();

        final User result = userService.findOne(id);

        assertNotNull(result);
        assertEquals(result.getId(), id);

        verifyAll();
    }

    @Test
    public void testFindByConfirmationToken() {
        final String confirmation = UUID.randomUUID().toString();
        User user = createUser(UUID.randomUUID(), true, "John", "Doe", TEST_EMAIL, Gender.M);
        user.setConfirmationToken(confirmation);

        expect(userRepository.findByConfirmationToken(confirmation))
                .andReturn(Optional.of(user))
                .times(2);

        replayAll();

        final User result = userService.findByConfirmationToken(confirmation);

        assertNotNull(result);
        assertEquals(result.getEmail(), TEST_EMAIL);
        assertEquals(result.getConfirmationToken(), confirmation);

        verifyAll();
    }

    @Test
    public void testActivateUser() {
        final UUID id = UUID.randomUUID();
        final User user = createUser(id, false, "John", "Doe", TEST_EMAIL, Gender.M);

        expect(userRepository.findById(id))
                .andReturn(Optional.of(user));

        Capture<User> userCapture = Capture.newInstance();
        expect(userRepository.save(capture(userCapture)))
                .andReturn(user);

        replayAll();

        userService.activateUser(id);

        assertTrue(user.isActive());

        verifyAll();
    }

    @Test
    public void testRegisterAndSendEmail() {

    }

    public User createUser(UUID id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    public User createUser(UUID id, boolean active, String firstName, String lastName, String email, Gender gender) {
        User user = new User();
        user.setId(id);
        user.setActive(active);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setGender(gender);
        return user;
    }
}