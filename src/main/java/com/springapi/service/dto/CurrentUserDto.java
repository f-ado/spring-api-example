package com.springapi.service.dto;

import com.springapi.domain.Gender;
import com.springapi.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CurrentUserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Gender gender;

    public CurrentUserDto(final User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.gender = user.getGender();
    }
}
