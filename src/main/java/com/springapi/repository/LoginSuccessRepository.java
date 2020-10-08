package com.springapi.repository;

import com.springapi.security.domain.LoginSuccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginSuccessRepository extends JpaRepository<LoginSuccess, Integer> {
}
