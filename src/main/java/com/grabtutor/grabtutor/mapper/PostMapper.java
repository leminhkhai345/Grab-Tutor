package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.request.PostRequest;
import com.grabtutor.grabtutor.dto.response.PostResponse;
import com.grabtutor.grabtutor.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(PostRequest postRequest);

    default void updatePostFromRequest(PostRequest postRequest,@MappingTarget Post post){
        if(postRequest.getDescription() != null && !postRequest.getDescription().isEmpty()){
            post.setDescription(postRequest.getDescription());
        }
        if(postRequest.getReward() > 0){
            post.setReward(postRequest.getReward());
        }
        if(postRequest.getImageUrl() != null && !postRequest.getImageUrl().isEmpty()){
            post.setImageUrl(postRequest.getImageUrl());
        }
    }

    PostResponse toPostResponse(Post post);
}
