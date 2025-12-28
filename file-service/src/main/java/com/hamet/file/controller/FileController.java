package com.hamet.file.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hamet.file.dto.response.ApiResponse;
import com.hamet.file.service.FileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FileController {

    FileService fileService;

    @PostMapping("/media/upload")
    public ApiResponse<Object> uploadMedia(
        @RequestParam("file") MultipartFile file
    ) {
        return ApiResponse.<Object>builder()
        .result(fileService.uploadFile(file))
        .build();
    }
    
}
