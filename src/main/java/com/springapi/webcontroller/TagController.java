package com.springapi.webcontroller;

import com.springapi.domain.Tag;
import com.springapi.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tags")
public class TagController extends BaseWebController {

    @Autowired
    private TagService tagService;

    @GetMapping("/all")
    public Resources<Tag> getAllTags(final Pageable page) {
        return okPagedResponse(tagService.getAll(page));
    }
}
