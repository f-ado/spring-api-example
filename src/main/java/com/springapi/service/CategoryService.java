package com.springapi.service;

import com.springapi.domain.Category;
import com.springapi.domain.Post;
import com.springapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private CategoryRepository repository;

    @Autowired
    public CategoryService(final CategoryRepository repository) {
        this.repository = repository;
    }

    public Page<Category> getAll(final Pageable page) {
        PageRequest nonSortablePageRequest = new PageRequest(page.getPageNumber(), page.getPageSize());

        Page postsPage = repository.findAll(nonSortablePageRequest);
        List<Post> posts = postsPage.getContent();

        return new PageImpl(posts, page, postsPage.getTotalElements());
    }
}
