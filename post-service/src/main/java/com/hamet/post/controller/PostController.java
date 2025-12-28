package com.hamet.post.controller;

import org.springframework.web.bind.annotation.RestController;

import com.hamet.post.dto.request.PostRequest;
import com.hamet.post.dto.response.ApiResponse;
import com.hamet.post.dto.response.PageResponse;
import com.hamet.post.dto.response.PostResponse;
import com.hamet.post.service.PostService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/my-post")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @PostMapping
    public ApiResponse<PostResponse> createPost(@RequestBody PostRequest request) {
        return ApiResponse.<PostResponse>builder()
            .result(postService.createPost(request))
            .build();
    }
    
    @GetMapping("")
    public ApiResponse<PageResponse<PostResponse>> getAllPost(
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<PostResponse>>builder()
            .result(postService.getAllPost(page,size))
            .build();
    }
    
}
