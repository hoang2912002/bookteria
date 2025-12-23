package com.hamet.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.hamet.identity.dto.request.PermissionRequest;
import com.hamet.identity.dto.response.PermissionResponse;
import com.hamet.identity.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
