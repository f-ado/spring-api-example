package com.springapi.webcontroller;

import com.springapi.authentication.CurrentUser;
import com.springapi.authentication.UserPrincipal;
import com.springapi.service.PostService;
import com.springapi.service.UserService;
import com.springapi.service.dto.PostDto;
import com.springapi.service.request.PostRequest;
import com.springapi.service.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class PostController extends BaseWebController {
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @PostMapping("/store")
    public ResponseEntity<ApiResponse> processRegistrationForm(@RequestBody final PostRequest postRequest,
                                                               @CurrentUser final UserPrincipal userPrincipal)
            throws EntityNotFoundException {
        if (postService.addNewPost(userPrincipal, postRequest)) {
           return new ResponseEntity<>(new ApiResponse(true, "Post created successfully"),
                   HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ApiResponse(false, "Something went wrong with post creation!"),
                HttpStatus.BAD_REQUEST);
    }

    @PutMapping("{id}/edit")
    public EntityModel<ResponseEntity<PostDto>> updatePost(@PathVariable("id") final Long id,
                                                        @RequestBody final PostRequest postRequest,
                                                        @CurrentUser final UserPrincipal userPrincipal) {
        return okResponse(postService.updatePost(id, postRequest));
    }

    @GetMapping("/all")
    public CollectionModel<PostDto> getAllPosts(@RequestParam(value = "search", required = false) final String search,
                                                @RequestParam(value = "owner_id", required = false) final UUID ownerId,
                                                Pageable page) {
        return okPagedResponse(postService.findAll(search, ownerId, page));
    }

    @GetMapping("/{id}")
    public EntityModel<ResponseEntity<PostDto>> getSinglePost(@PathVariable("id") final Long id)
            throws EntityNotFoundException {
        return okResponse(postService.getById(id));
    }
}
