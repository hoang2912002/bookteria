package com.hamet.post.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.hamet.post.entity.Post;

public interface PostRepository extends MongoRepository<Post, String> {
    // List<Post> findAllByUserId(String userId);
    Page findAllByUserId(String userId, Pageable pageable);
}
