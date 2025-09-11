package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.request.PostRequest;
import com.grabtutor.grabtutor.dto.response.PostResponse;
import com.grabtutor.grabtutor.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(PostRequest postRequest);
    void updatePostFromRequest(PostRequest postRequest,@MappingTarget Post post);
    PostResponse toPostResponse(Post post);
}
