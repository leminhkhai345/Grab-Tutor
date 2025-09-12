package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.PostRequest;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.dto.response.PostResponse;

import java.util.List;

public interface PostService {
    PostResponse addPost(PostRequest postRequest);
    PostResponse getPostByPostId(String postId);
    PostResponse updatePost(String postId, PostRequest postRequest);
    void deletePost(String postId);
    List<PostResponse> getPostByUserId(String userId);
    PageResponse<?> getAllPosts(int pageNo, int pageSize, String... sorts);

}
