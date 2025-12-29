package com.hamet.file.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hamet.file.dto.response.FileData;
import com.hamet.file.dto.response.FileInfoResponse;
import com.hamet.file.dto.response.FileResponse;
import com.hamet.file.entity.FileManagement;
import com.hamet.file.exception.AppException;
import com.hamet.file.exception.ErrorCode;
import com.hamet.file.mapper.FileManagementMapper;
import com.hamet.file.repository.FileManagementRepository;
import com.hamet.file.repository.FileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.var;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FileService {
    FileRepository fileRepository;
    FileManagementRepository fileManagementRepository;
    FileManagementMapper fileManagementMapper;

    public FileResponse uploadFile(MultipartFile file){
        // Store file
        FileInfoResponse fResponse = fileRepository.store(file);
        
        // Save in mongodb
        FileManagement fileManagement = fileManagementMapper.toFileManagement(fResponse);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        fileManagement.setOwnerId(userId);
        
        FileManagement rFileManagement = fileManagementRepository.save(fileManagement);
        return FileResponse.builder()
            .url(fResponse.getUrl())
            .originalFileName(file.getOriginalFilename())
            .build();
    }

    public FileData download(String fileName){
        FileManagement fileManagement = fileManagementRepository.findById(fileName).orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_FOUND));
        return new FileData(fileManagement.getContentType(), fileRepository.read(fileManagement));
    }
}
