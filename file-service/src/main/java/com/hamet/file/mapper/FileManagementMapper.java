package com.hamet.file.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.hamet.file.dto.response.FileInfoResponse;
import com.hamet.file.entity.FileManagement;

@Mapper(
    componentModel = "spring"
)
public interface FileManagementMapper {
    @Mapping(target = "id", source = "name")
    FileManagement toFileManagement(FileInfoResponse fileInfoResponse);
}
