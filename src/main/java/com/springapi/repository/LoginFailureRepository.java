package com.springapi.repository;

import com.springapi.domain.User;
import com.springapi.security.domain.LoginFailure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface LoginFailureRepository extends JpaRepository<LoginFailure, Integer> {
    List<LoginFailure> findAllByUserAndCreatedDateIsAfter(User user, Timestamp timestamp);
}
