package com.hamet.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.hamet.identity.dto.request.UserCreationRequest;
import com.hamet.identity.dto.request.UserUpdateRequest;
import com.hamet.identity.dto.response.UserResponse;
import com.hamet.identity.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
