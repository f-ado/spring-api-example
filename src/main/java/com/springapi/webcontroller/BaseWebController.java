package com.springapi.webcontroller;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
    protected static <T> EntityModel<T> okResponse(final T o) {
        EntityModel<T> resource = new EntityModel<>(o);
        return resource;
    }

    /**
     * Returns a Paginated response with a page data.
     *
     * @param data Page data.
     * @param <T> Type of data (within page data).
     * @return Paginated response.
     */
    protected static <T> CollectionModel<T> okPagedResponse(final Page<T> data) {
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                data.getSize(),
                data.getNumber(),
                data.getTotalElements(),
                data.getTotalPages()
        );
        return new PagedModel<>(data.getContent(), pageMetadata);
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
