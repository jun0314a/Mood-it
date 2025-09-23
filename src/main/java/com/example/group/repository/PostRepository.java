// src/main/java/com/example/Group/repository/PostRepository.java
package com.example.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.group.entity.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByGroupId(Integer groupId);

    void deleteByGroupId(Integer groupId);
}
