package com.springapi.service.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class LoginRequest {

    @Getter
    @Setter
    @NotBlank
    private String usernameOrEmail;

    @Getter
    @Setter
    @NotBlank
    private String password;
}
