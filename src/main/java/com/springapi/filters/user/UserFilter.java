package com.springapi.filters.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFilter {

    private Boolean active;

    private String search;

    private String gender;
}
