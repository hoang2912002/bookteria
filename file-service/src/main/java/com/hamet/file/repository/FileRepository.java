package com.hamet.file.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hamet.file.dto.response.FileInfoResponse;
import com.hamet.file.entity.FileManagement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

@Repository
public class FileRepository {

    @Value("${app.file.storage-dir}")
    String storageDir;

    @Value("${app.file.download-prefix}")
    String urlPrefix;

    public FileInfoResponse store(MultipartFile file) {
        try{
            Path folder = Paths.get(storageDir);
            String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String fileName = Objects.isNull(fileExtension) ? 
                UUID.randomUUID().toString() :
                UUID.randomUUID().toString() + "." + fileExtension; 

            Path filePath = folder.resolve(fileName).normalize().toAbsolutePath();

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return FileInfoResponse.builder()
                .name(fileName)
                .size(file.getSize())
                .contentType(file.getContentType())
                .md5Checksum(DigestUtils.md5DigestAsHex(file.getInputStream()))
                .path(filePath.toString())
                .url(urlPrefix + fileName)
                .build();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            throw e;
        }
        return null;
    }

    public Resource read(FileManagement fileManagement){
        try {
            var data = Files.readAllBytes(Path.of(fileManagement.getPath()));
            return new ByteArrayResource(data);
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw e;
        }
        return null;
    }
}
