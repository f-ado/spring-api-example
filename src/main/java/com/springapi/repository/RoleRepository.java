package com.springapi.repository;

import com.springapi.security.domain.Role;
import com.springapi.security.domain.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(final RoleName roleName);
}
