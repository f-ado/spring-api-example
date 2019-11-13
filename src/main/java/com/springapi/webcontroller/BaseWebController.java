package com.springapi.webcontroller;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseWebController {

    /**
     * Returns a 200 response with given instance.
     *
     * @param o Object instance to return.
     * @param <T> Type of passed object instance.
     * @return Response entity with 200 status and content.
     */
    protected static <T> Resource<T> okResponse(final T o) {
        Resource<T> resource = new Resource<>(o);
        return resource;
    }

    /**
     * Returns a Paginated response with a page data.
     *
     * @param data Page data.
     * @param <T> Type of data (within page data).
     * @return Paginated response.
     */
    protected static <T> Resources<T> okPagedResponse(final Page<T> data) {
        PagedResources.PageMetadata pageMetadata = new PagedResources.PageMetadata(
                data.getSize(),
                data.getNumber(),
                data.getTotalElements(),
                data.getTotalPages()
        );
        return new PagedResources<>(data.getContent(), pageMetadata);
    }

    /**
     * Returns response with Created status with given resource.
     *
     * @param resource Resource.
     * @param <T> Resource type.
     * @return 201 Created Status.
     */
    protected static <T> ResponseEntity<T> createdResponse(final T resource) {
        ResponseEntity.BodyBuilder response = ResponseEntity.status(HttpStatus.CREATED);
        return response.body(resource);
    }

}
