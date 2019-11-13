package com.springapi.bootstrap;

import com.springapi.domain.*;
import com.springapi.repository.CategoryRepository;
import com.springapi.repository.PostRepository;
import com.springapi.repository.TagRepository;
import com.springapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private UserRepository userRepository;

    private PostRepository postRepository;

    private TagRepository tagRepository;

    private CategoryRepository categoryRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(final UserRepository userRepository,
                      final PostRepository postRepository,
                      final TagRepository tagRepository,
                      final CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
        initData();
    }

    private void initData() {
        createUser("john_doe","John", "Doe",  "johndoe@example.com",
                passwordEncoder.encode("password"), Gender.M, true);

        createUser("jane_doe", "Jane", "Doe", "janedoe@example.com",
                "1234", Gender.F, true);

        createUser("james_hetfield", "James", "Hetfield", "hetfield@example.com",
                passwordEncoder.encode("password"), Gender.M, false);

        createUser("jack_london", "Jack", "London", "jackL@example.com",
                passwordEncoder.encode("password"), Gender.M, false);

        createUser("radioactive", "Marie", "Curie", "mcurie@example.com",
                passwordEncoder.encode("password"), Gender.F, true);

        createUser("ww", "Walt", "Whitman", "ww@example.com",
                passwordEncoder.encode("password"), Gender.M, true);

        createUser("vwoolf", "Virginia", "Woolf", "vwoolf@example.com",
                passwordEncoder.encode("password"), Gender.F, true);

        createUser("bruced", "Bruce", "Dickinson", "eddiethegreat@example.com",
                passwordEncoder.encode("password"), Gender.M, true);

        createUser("a_mariner", "Ancient", "Mariner", "amariner@example.com",
                passwordEncoder.encode("password"), Gender.M, true);

        createUser("thequeenofcrime", "Agatha", "Cristie", "achristie@example.com",
                passwordEncoder.encode("password"), Gender.F, true);

        createUser("particle", "Stephen", "Hawking", "hawking@example.com",
                passwordEncoder.encode("password"), Gender.M, true);

        createUser("hopper", "Grace", "Hopper", "ghopper@example.com",
                passwordEncoder.encode("password"), Gender.F, true);

        createUser("torvalds", "Linus", "Torvalds", "linux@example.com",
                passwordEncoder.encode("password"), Gender.M, true);

        createUser("annie", "Annie", "Easley", "easley@example.com",
                passwordEncoder.encode("password"), Gender.F, true);

        createPost(userRepository.findByEmail("hetfield@example.com").get());
        createPost(userRepository.findByEmail("eddiethegreat@example.com").get());
    }

    public void createUser(final String username, final String firstName, final String lastName, final String email, final String password,
                           final Gender gender, final boolean active) {
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
        Role r = new Role();
        r.setId(1L);
        r.setName(RoleName.ROLE_USER);
        Set<Role> roles = new HashSet<>();
        roles.add(r);
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
