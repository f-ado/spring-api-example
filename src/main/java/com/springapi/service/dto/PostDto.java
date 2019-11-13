package com.springapi.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springapi.domain.Category;
import com.springapi.domain.Post;
import com.springapi.domain.Tag;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.hateoas.core.Relation;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Relation(collectionRelation = "posts")
public class PostDto {
    private Long id;
    private String title;
    private String videoUrl;
    private String image;
    private String body;
    private UserDto user;
    private Set<Category> categories = new HashSet<>();
    private Set<Tag> tags = new HashSet<>();
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;

    public PostDto() {
    }

    public PostDto(final Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.image = StringUtils.defaultIfBlank(post.getImage(), "default");
        this.body = post.getBody();
        this.categories = post.getCategories();
        this.tags = post.getTags();
        this.user = new UserDto(post.getUser());
        this.createdAt = post.getCreatedAt().toString();
        this.updatedAt = post.getUpdatedAt().toString();
    }
}
