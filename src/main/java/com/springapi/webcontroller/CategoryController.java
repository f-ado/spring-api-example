package com.springapi.webcontroller;

import com.springapi.domain.Category;
import com.springapi.security.permissions.CategoryCreatePermission;
import com.springapi.security.permissions.CategoryReadPermission;
import com.springapi.service.CategoryService;
import com.springapi.service.request.CategoryRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController extends BaseWebController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @CategoryReadPermission
    @GetMapping("/all")
    public CollectionModel<Category> getAllCategories(final Pageable page) {
        return okPagedResponse(categoryService.getAll(page));
    }

    @CategoryCreatePermission
    @PostMapping("/create")
    public EntityModel<Category> createCategory(final @Valid @RequestBody CategoryRequest request) {
        return okResponse(categoryService.createCategory(request));
    }
}
