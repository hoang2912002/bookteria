package com.hamet.post.service;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hamet.post.dto.request.PostRequest;
import com.hamet.post.dto.response.PageResponse;
import com.hamet.post.dto.response.PostResponse;
import com.hamet.post.entity.Post;
import com.hamet.post.mapper.PostMapper;
import com.hamet.post.repository.PostRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.var;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    DateTimeFormatter dateTimeFormatter;

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

    public PageResponse<PostResponse> getAllPost(int page, int size){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Post> pageData = postRepository.findAllByUserId(authentication.getName(), pageable);
        List<PostResponse> postResponse = 
            pageData.getContent().stream()
            .map(
                post -> {
                    PostResponse postRes = postMapper.toPostResponse(post);
                    postRes.setCreated(dateTimeFormatter.format(post.getCreatedDate()));
                    return postRes;
                } 
            ).toList();

        return PageResponse.<PostResponse>builder()
            .totalPages(pageData.getTotalPages())
            .pageSize(pageData.getSize())
            .currentPage(page)
            .totalElements(pageData.getTotalElements())
            .data(postResponse)
            .build();
           
    }
}
