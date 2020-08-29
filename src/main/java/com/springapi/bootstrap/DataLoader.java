package com.springapi.bootstrap;

import com.springapi.security.domain.Authority;
import com.springapi.domain.Category;
import com.springapi.domain.Gender;
import com.springapi.domain.Post;
import com.springapi.security.domain.Role;
import com.springapi.security.domain.RoleName;
import com.springapi.domain.Tag;
import com.springapi.domain.User;
import com.springapi.repository.AuthorityRepository;
import com.springapi.repository.CategoryRepository;
import com.springapi.repository.PostRepository;
import com.springapi.repository.RoleRepository;
import com.springapi.repository.TagRepository;
import com.springapi.repository.UserRepository;
import com.springapi.security.permissions.CategoryPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DataLoader implements CommandLineRunner {
    private UserRepository userRepository;

    private PostRepository postRepository;

    private TagRepository tagRepository;

    private CategoryRepository categoryRepository;

    private RoleRepository roleRepository;

    private AuthorityRepository authorityRepository;

    final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(
        final UserRepository userRepository,
        final PostRepository postRepository,
        final TagRepository tagRepository,
        final CategoryRepository categoryRepository,
        final RoleRepository roleRepository,
        final AuthorityRepository authorityRepository,
        final PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        initData();
    }

    private void initData() {
        Authority readCategory = authorityRepository.save(Authority.builder()
            .permission(CategoryPermissions.READ.getValue()).build());
        Authority updateCategory = authorityRepository.save(Authority.builder()
            .permission(CategoryPermissions.UPDATE.getValue()).build());
        Authority createCategory = authorityRepository.save(Authority.builder()
            .permission(CategoryPermissions.CREATE.getValue()).build());
        Authority deleteCategory = authorityRepository.save(Authority.builder()
            .permission(CategoryPermissions.DELETE.getValue()).build());

        Role roleUser = roleRepository.save(Role.builder().id(1).name(RoleName.ROLE_USER).build());
        Role roleAdmin = roleRepository.save(Role.builder().id(2).name(RoleName.ROLE_ADMIN).build());

        roleAdmin.setAuthorities(
            Stream.of(createCategory, updateCategory, readCategory, deleteCategory).collect(Collectors.toSet())
        );
        roleUser.setAuthorities(Stream.of(readCategory).collect(Collectors.toSet()));

        roleRepository.saveAll(Arrays.asList(roleAdmin, roleUser));

        createUser("john_doe","John", "Doe",  "johndoe@example.com",
                passwordEncoder.encode("password"), Gender.M, true, roleAdmin);

        createUser("jane_doe", "Jane", "Doe", "janedoe@example.com",
                passwordEncoder.encode("password"), Gender.F, true, roleAdmin);

        createUser("james_hetfield", "James", "Hetfield", "hetfield@example.com",
                passwordEncoder.encode("password"), Gender.M, false, roleUser);

        createUser("jack_london", "Jack", "London", "jackL@example.com",
                passwordEncoder.encode("password"), Gender.M, false, roleUser);

        createUser("radioactive", "Marie", "Curie", "mcurie@example.com",
                passwordEncoder.encode("password"), Gender.F, true, roleUser);

        createUser("ww", "Walt", "Whitman", "ww@example.com",
                passwordEncoder.encode("password"), Gender.M, true, roleUser);

        createUser("vwoolf", "Virginia", "Woolf", "vwoolf@example.com",
                passwordEncoder.encode("password"), Gender.F, true, roleUser);

        createUser("bruced", "Bruce", "Dickinson", "eddiethegreat@example.com",
                passwordEncoder.encode("password"), Gender.M, true, roleUser);

        createUser("a_mariner", "Ancient", "Mariner", "amariner@example.com",
                passwordEncoder.encode("password"), Gender.M, true, roleUser);

        createUser("thequeenofcrime", "Agatha", "Cristie", "achristie@example.com",
                passwordEncoder.encode("password"), Gender.F, true, roleUser);

        createUser("particle", "Stephen", "Hawking", "hawking@example.com",
                passwordEncoder.encode("password"), Gender.M, true, roleUser);

        createUser("hopper", "Grace", "Hopper", "ghopper@example.com",
                passwordEncoder.encode("password"), Gender.F, true, roleUser);

        createUser("torvalds", "Linus", "Torvalds", "linux@example.com",
                passwordEncoder.encode("password"), Gender.M, true, roleUser);

        createUser("annie", "Annie", "Easley", "easley@example.com",
                passwordEncoder.encode("password"), Gender.F, true, roleUser);

        createPost(userRepository.findByEmail("hetfield@example.com").get());
        createPost(userRepository.findByEmail("eddiethegreat@example.com").get());
    }

    public void createUser(
        final String username,
        final String firstName,
        final String lastName,
        final String email,
        final String password,
        final Gender gender,
        final boolean active,
        final Role userRole
    ) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setGender(gender);
        user.setActive(active);
        user.setConfirmationToken(UUID.randomUUID().toString());
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);
    }

    public void createPost(final User user) {
        Set<Category> categorySet = new HashSet<>();
        Category c1 = categoryRepository.findByName("Food").get();
        Category c2 = categoryRepository.findByName("Sport").get();
        categorySet.add(c1);
        categorySet.add(c2);

        Set<Tag> tags = new HashSet<>();
        Tag t1 = tagRepository.findByName("Pizza").get();
        tags.add(t1);

        Post post = new Post();
        post.setTitle(user.getFirstName() + "'s post - " + postRepository.count());
        post.setBody("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        post.setUser(user);
        post.setCategories(categorySet);
        post.setTags(tags);
        postRepository.save(post);
    }
}
