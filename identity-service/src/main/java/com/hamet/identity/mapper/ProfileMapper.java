package com.hamet.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.hamet.identity.dto.request.ProfileCreateRequest;
import com.hamet.identity.dto.request.UserCreationRequest;

@Mapper(
    componentModel = "spring"
)
public interface ProfileMapper {
    ProfileCreateRequest toProfileCreateRequest(UserCreationRequest request);
}
