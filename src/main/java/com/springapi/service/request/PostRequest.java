package com.springapi.service.request;

import com.springapi.domain.Category;
import com.springapi.domain.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PostRequest {
    private String title;

    private String body;

    private Set<Category> categories;

    private Set<Tag> tags;
}
