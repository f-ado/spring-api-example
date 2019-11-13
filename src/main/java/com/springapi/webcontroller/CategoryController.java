package com.springapi.webcontroller;

import com.springapi.domain.Category;
import com.springapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController extends BaseWebController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/all")
    public Resources<Category> getAllTags(final Pageable page) {
        return okPagedResponse(categoryService.getAll(page));
    }
}
