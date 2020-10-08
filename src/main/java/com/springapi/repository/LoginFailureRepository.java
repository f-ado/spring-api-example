package com.springapi.repository;

import com.springapi.security.domain.LoginFailure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginFailureRepository extends JpaRepository<LoginFailure, Integer> {
}
