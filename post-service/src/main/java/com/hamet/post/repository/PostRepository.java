package com.hamet.post.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hamet.post.entity.Post;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findAllByUserId(String userId);
}
