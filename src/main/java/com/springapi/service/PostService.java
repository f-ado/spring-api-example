package com.springapi.service;

import com.springapi.authentication.UserPrincipal;
import com.springapi.domain.Post;
import com.springapi.filters.post.PostFilter;
import com.springapi.filters.specifications.post.PostSpecification;
import com.springapi.repository.PostRepository;
import com.springapi.service.dto.PostDto;
import com.springapi.service.exception.ResourceNotFoundException;
import com.springapi.service.request.PostRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static final String ENTITY_NOT_FOUND_MESSAGE = "Entity not found!";

    private PostRepository repository;
    private UserService userService;

    @Autowired
    public PostService(final PostRepository repository, final UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }


    public boolean addNewPost(final UserPrincipal userPrincipal, final PostRequest postRequest) {
        try {
            Post post = new Post();
            post.setTitle(postRequest.getTitle());
            post.setBody(postRequest.getBody());
            post.setTags(postRequest.getTags());
            post.setCategories(postRequest.getCategories());
            post.setUser(userService.findOne(userPrincipal.getId()));
            repository.save(post);
            return true;
        } catch (Exception e) {
            LOGGER.debug("Error with post creation!\n" + e.getMessage());
            return false;
        }
    }

    public ResponseEntity<PostDto> updatePost(final Long id, final PostRequest postRequest)
            throws EntityNotFoundException {
        Optional<Post> post = repository.findById(id);
        if(post.isPresent()) {
            post.get().setTitle(postRequest.getTitle());
            post.get().setBody(postRequest.getBody());
            post.get().setCategories(postRequest.getCategories());
            post.get().setTags(postRequest.getTags());
            PostDto updatedPost = new PostDto(repository.save(post.get()));
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } else {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE);
        }
    }

    public Page<PostDto> findAll(final String search, final UUID ownerId, final Pageable page)
            throws ResourceNotFoundException {
        PostFilter filter = PostFilter
                .builder()
                .search(search)
                .ownerId(ownerId)
                .build();

        PageRequest nonSortablePageRequest = new PageRequest(page.getPageNumber(), page.getPageSize());

        Page<Post> postsPage = repository.findAll(new PostSpecification(filter), nonSortablePageRequest);
        List<Post> posts = postsPage.getContent();

        return new PageImpl<>(posts.stream().map(PostDto::new).collect(Collectors.toList()),
                page, postsPage.getTotalElements());

    }

    public ResponseEntity<PostDto> getById(final Long id) {
        Optional<Post> post = repository.findById(id);

        if (post.isPresent()) {
            return new ResponseEntity<>(new PostDto(post.get()), HttpStatus.OK);
        } else {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE);
        }
    }
}
