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

        if(postRequest.getImageUrl() != null && !postRequest.getImageUrl().isEmpty()){
            post.setImageUrl(postRequest.getImageUrl());
        }
    }

    default PostResponse toPostResponse(Post post) {
        if (post == null) {
            return null;
        } else {
            PostResponse.PostResponseBuilder postResponse = PostResponse.builder();
            postResponse.id(post.getId());
            postResponse.imageUrl(post.getImageUrl());
            postResponse.description(post.getDescription());
            if (post.getStatus() != null) {
                postResponse.status(post.getStatus().name());
            }
            postResponse.userId(post.getUser().getId());
            postResponse.subjectId(post.getSubject().getId());
            postResponse.isDeleted(post.isDeleted());
            postResponse.createdAt(post.getCreatedAt());
            postResponse.updatedAt(post.getUpdatedAt());
            return postResponse.build();
        }
    }
}
