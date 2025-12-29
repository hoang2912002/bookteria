package com.hamet.file.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hamet.file.entity.FileManagement;

public interface FileManagementRepository extends MongoRepository<FileManagement, String>{
    
}
