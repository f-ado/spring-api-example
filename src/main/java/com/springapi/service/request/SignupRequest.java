package com.springapi.service.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignupRequest {

    @NotBlank
    @Size(min = 4, max = 40)
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 40)
    @JsonProperty("last_name")
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 15)
    @JsonProperty("username")
    private String username;

    @NotBlank
    @Size(min = 6, max = 20)
    @JsonProperty("password")
    private String password;

    @NotBlank
    @Size(max = 40)
    @Email
    @JsonProperty("email")
    private String email;
}
