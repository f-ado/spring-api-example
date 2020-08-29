package com.springapi.service.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CategoryRequest {
    @NotNull
    @NotEmpty
    private String name;
}
