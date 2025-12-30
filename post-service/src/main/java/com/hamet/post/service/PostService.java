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
import com.hamet.post.dto.response.UserProfileResponse;
import com.hamet.post.entity.Post;
import com.hamet.post.mapper.PostMapper;
import com.hamet.post.repository.PostRepository;
import com.hamet.post.repository.httpClient.ProfileClient;

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
    ProfileClient profileClient;

    public PostResponse createPost(PostRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserProfileResponse userProfile = null;
        try {
            userProfile = profileClient.getProfileByUserId(authentication.getName()).getResult();
            
        } catch (Exception e) {
            log.error("Error get profile by user id", e);
        }
        Post post = Post.builder()
            .content(request.getContent())
            .createdDate(Instant.now())
            .modifiedDate(Instant.now())
            .userId(authentication.getName())
            .build();

        String username = userProfile != null ? userProfile.getFirstName() + userProfile.getLastName() : null;
        PostResponse postResponse = postMapper.toPostResponse(postRepository.save(post));
        postResponse.setCreated(dateTimeFormatter.format(post.getCreatedDate()));
        postResponse.setUsername(username);
        return postResponse;
    }

    public PageResponse<PostResponse> getAllPost(int page, int size){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserProfileResponse userProfile = null;
        try {
            userProfile = profileClient.getProfileByUserId(authentication.getName()).getResult();
            
        } catch (Exception e) {
            log.error("Error get profile by user id", e);
        }
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Post> pageData = postRepository.findAllByUserId(authentication.getName(), pageable);
        String username = userProfile != null ? userProfile.getFirstName() + userProfile.getLastName() : null;
        List<PostResponse> postResponse = 
            pageData.getContent().stream()
            .map(
                post -> {
                    PostResponse postRes = postMapper.toPostResponse(post);
                    postRes.setCreated(dateTimeFormatter.format(post.getCreatedDate()));
                    postRes.setUsername(username);
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
