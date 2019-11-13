package com.springapi.service.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernameEmailAvailability {
    private boolean available;

    public UsernameEmailAvailability(final boolean available) {
        this.available = available;
    }
}
