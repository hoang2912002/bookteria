package com.hamet.file.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hamet.file.dto.response.ApiResponse;
import com.hamet.file.dto.response.FileData;
import com.hamet.file.dto.response.FileResponse;
import com.hamet.file.service.FileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.var;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.http.HttpHeaders;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping
public class FileController {

    FileService fileService;

    @PostMapping("/media/upload")
    public ApiResponse<FileResponse> uploadMedia(
        @RequestPart("file") MultipartFile file
    ) {
        return ApiResponse.<FileResponse>builder()
        .result(fileService.uploadFile(file))
        .build();
    }
    
    @GetMapping("/media/download/{fileName}")
    public ResponseEntity<Resource> downloadMedia(@PathVariable("fileName") String fileName) {
        FileData fileData = fileService.download(fileName);

        // HttpHeaders.CONTENT_DISPOSITION -> trả cho trình duyệt 1 file name khác cho dễ đọc
        // HttpHeaders.CACHE_CONTROL -> cache ảnh ở browser để tăng hiệu năng đỡ load lại ảnh
        return ResponseEntity.ok().header(
            HttpHeaders.CONTENT_TYPE,
            fileData.contentType()
        ).body(
            fileData.resource()
        );
    }
    
}
