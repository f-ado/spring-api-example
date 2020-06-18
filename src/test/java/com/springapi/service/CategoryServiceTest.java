package com.springapi.service;

import com.springapi.domain.Category;
import com.springapi.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @InjectMocks
    private CategoryService service;

    @Mock
    private CategoryRepository repository;

    private PageRequest nonSortablePageRequest = PageRequest.of(1, 20);

    @Test
    void getAll() {
        List<Category> categoryList = Arrays.asList(createCategory(1L, "c1"), createCategory(2L, "c2"));
        Page<Category> categoryPage = new PageImpl(categoryList);

        when(repository.findAll(nonSortablePageRequest)).thenReturn(categoryPage);

        Page<Category> result = service.getAll(PageRequest.of(1, 20));
        verify(repository).findAll(nonSortablePageRequest);

        assertNotNull(result.getContent());
        assertEquals(result.getContent().size(), 2);
        assertEquals(result.getContent().get(0).getName(), "c1");
    }

    private Category createCategory(Long id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setPosts(new HashSet<>());
        return category;
    }
}