package com.springapi.service;

import com.springapi.domain.Category;
import com.springapi.domain.Tag;
import com.springapi.repository.TagRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository repository;

    @InjectMocks
    private TagService service;

    private PageRequest nonSortablePageRequest = PageRequest.of(1, 20);

    @Test
    void getAll() {
        List<Tag> tagList = Arrays.asList(createTag(1L, "T1"), createTag(2L, "T2"));
        Page<Tag> tagPage = new PageImpl(tagList);

        when(repository.findAll(nonSortablePageRequest)).thenReturn(tagPage);
        Page<Tag> result = service.getAll(nonSortablePageRequest);

        verify(repository).findAll(nonSortablePageRequest);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
    }

    private Tag createTag(Long id, String name) {
        Tag tag = new Tag();
        tag.setId(id);
        tag.setName(name);
        tag.setPosts(new HashSet<>());
        return tag;
    }
}