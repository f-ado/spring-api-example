package com.springapi.repository;

import com.springapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends
        CrudRepository<User, UUID>,
        JpaRepository<User, UUID>,
        JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(final String email);

    Optional<User> findByUsername(final String username);

    Optional<User> findByConfirmationToken(final String confirmationToken);

    Optional<User> findByUsernameOrEmail(final String username, final String email);

    Boolean existsByEmail(final String email);

    Boolean existsByUsername(final String username);

}
