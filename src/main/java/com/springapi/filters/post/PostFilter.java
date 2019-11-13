package com.springapi.filters.post;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PostFilter {
    private String search;
    private UUID ownerId;
}
