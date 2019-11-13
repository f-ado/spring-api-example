package com.springapi.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

public class BaseService<T, ID, R extends Repository<T, ID>> {
    public static final String ENTITY_NOT_FOUND_MESSAGE = "Entity not found";

    protected R repository;

    public BaseService(R repository) {
        this.repository = repository;
    }

    public T getById(final ID id) throws EntityNotFoundException {
        final Optional<T> baseModel = getRepository().findById(id);
        if (baseModel.isPresent()) {
            return baseModel.get();
        } else {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE);
        }
    }

    public List<T> findAll() {
        return getRepository().findAll();
    }

    public T save(final T baseModel) {
        return getRepository().save(baseModel);
    }

    public void delete(final ID id) {
        getRepository().deleteById(id);
    }

    protected JpaRepository<T, ID> getRepository() {
        return (JpaRepository<T, ID>) repository;
    }
}
