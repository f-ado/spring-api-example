package com.springapi.repository;

import com.springapi.domain.Post;
import com.springapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends
        CrudRepository<Post, Long>,
        JpaRepository<Post, Long>,
        JpaSpecificationExecutor<Post> {

        List<Post> findByUser(final User user);
}
