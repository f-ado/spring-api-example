package com.springapi.webcontroller;

import com.springapi.domain.Category;
import com.springapi.service.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController extends BaseWebController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasAuthority('category.read')")
    @GetMapping("/all")
    public CollectionModel<Category> getAllTags(final Pageable page) {
        return okPagedResponse(categoryService.getAll(page));
    }
}
