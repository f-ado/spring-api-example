package com.springapi.service;

import com.springapi.EasyMockExtension;
import com.springapi.domain.Category;
import com.springapi.repository.CategoryRepository;
import org.easymock.EasyMock;
import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ExtendWith(EasyMockExtension.class)
class CategoryServiceTest {
    private CategoryService service;

    @Mock
    private CategoryRepository repository;

    @BeforeEach
    void setUp() {
        service = new CategoryService(repository);
    }

    @Test
    void getAll() {
        List<Category> categoryList = Arrays.asList(
                createCategory(1L, "c1"), createCategory(2L, "c2")
        );

        Page<Category> categoryPage = new PageImpl(categoryList);

        expect(repository.findAll(PageRequest.of(1, 20)))
                .andReturn(categoryPage);

        EasyMock.replay(repository);

        Page<Category> result = repository.findAll(PageRequest.of(1, 20));

        assertNotNull(result.getContent());
        assertEquals(result.getContent().size(), 2);
        assertEquals(result.getContent().get(0).getName(), "c1");

        EasyMock.verify(repository);
    }

    private Category createCategory(Long id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setPosts(new HashSet<>());
        return category;
    }
}