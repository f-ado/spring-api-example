package com.springapi.service.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    private Boolean success;
    private String message;

    public ApiResponse(final Boolean success, final String message) {
        this.success = success;
        this.message = message;
    }
}
