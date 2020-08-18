package com.springapi.service.dto;

import com.springapi.domain.Gender;
import com.springapi.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private boolean active;
    private Gender gender;

    public UserDto() {
    }

    public UserDto(final User user) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        username = user.getUsername();
        email = user.getEmail();
        active = user.isActive();
        gender = user.getGender();
    }
}
