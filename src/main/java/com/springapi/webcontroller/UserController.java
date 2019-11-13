package com.springapi.webcontroller;

import com.springapi.authentication.CurrentUser;
import com.springapi.authentication.UserPrincipal;
import com.springapi.filters.user.UserFilter;
import com.springapi.service.UserService;
import com.springapi.service.dto.CurrentUserDto;
import com.springapi.service.dto.UserDto;
import com.springapi.service.response.UsernameEmailAvailability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseWebController {
    @Autowired
    private UserService userService;

    @GetMapping("/filter")
    public Resources<UserDto> filterUsers(
            @RequestParam(value = "search", required = false) final String search,
            @RequestParam(value = "active", required = false) final Boolean active,
            @RequestParam(value = "gender", required = false) final String gender,
            @PageableDefault(size = 5) Pageable page
    ) {
        UserFilter.UserFilterBuilder userFilterBuilder = UserFilter.builder()
                .search(search)
                .active(active)
                .gender(gender);

        return okPagedResponse(userService.getFilteredUsers(userFilterBuilder.build(), page));
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @ResponseBody
    @GetMapping
    public Resources<UserDto> getAllUsers(@CurrentUser final UserPrincipal currentUser, final Pageable page) {

        return okPagedResponse(userService.getAllUsers(page));
    }

    @GetMapping("/{id}")
    public Resource<UserDto> getById(@PathVariable("id") final UUID id) {
        return okResponse(userService.getUser(id));
    }

    @GetMapping("/checkUsernameAvailability")
    public UsernameEmailAvailability checkUsernameAvailability(
            @RequestParam(value = "username") final String username
    ) {
        Boolean isAvailable = !userService.existsByUsername(username);
        return new UsernameEmailAvailability(isAvailable);
    }

    @GetMapping("/checkEmailAvailability")
    public UsernameEmailAvailability checkEmailAvailability(@RequestParam(value = "email") final String email) {
        Boolean isAvailable = !userService.existsByEmail(email);
        return new UsernameEmailAvailability(isAvailable);
    }

    @GetMapping("/me")
    public Resource<CurrentUserDto> getCurrentUser(@CurrentUser final UserPrincipal userPrincipal) {
        return okResponse(userService.getCurrentUser(userPrincipal));
    }

}
