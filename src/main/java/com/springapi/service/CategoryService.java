package com.springapi.service;

import com.springapi.domain.Category;
import com.springapi.repository.CategoryRepository;
import com.springapi.service.request.CategoryRequest;
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
        PageRequest nonSortablePageRequest = PageRequest.of(page.getPageNumber(), page.getPageSize());

        Page categoryPage = repository.findAll(nonSortablePageRequest);
        List<Category> categories = categoryPage.getContent();

        return new PageImpl(categories, page, categoryPage.getTotalElements());
    }

    public Category createCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        return repository.save(category);
    }
}
