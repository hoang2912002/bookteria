package com.hamet.post.mapper;

import org.mapstruct.Mapper;

import com.hamet.post.dto.response.PostResponse;
import com.hamet.post.entity.Post;

@Mapper(
    componentModel = "spring"
)
public interface PostMapper {
    PostResponse toPostResponse(Post post);
}
