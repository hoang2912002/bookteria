package com.hamet.post.service;

import java.time.Instant;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hamet.post.dto.request.PostRequest;
import com.hamet.post.dto.response.PostResponse;
import com.hamet.post.entity.Post;
import com.hamet.post.mapper.PostMapper;
import com.hamet.post.repository.PostRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {
    PostRepository postRepository;
    PostMapper postMapper;

    public PostResponse createPost(PostRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Post post = Post.builder()
            .content(request.getContent())
            .createdDate(Instant.now())
            .modifiedDate(Instant.now())
            .userId(authentication.getName())
            .build();
        return postMapper.toPostResponse(postRepository.save(post));
    }

    public List<PostResponse> getAllPost(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return
            postRepository.findAllByUserId(authentication.getName()).stream()
            .map(postMapper::toPostResponse).toList();
    }
}
