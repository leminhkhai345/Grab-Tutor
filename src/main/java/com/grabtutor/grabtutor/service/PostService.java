package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.AcceptPostRequest;
import com.grabtutor.grabtutor.dto.request.PostRequest;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.dto.response.PostResponse;
import com.grabtutor.grabtutor.entity.Post;
import org.springframework.data.domain.Page;


import java.io.IOException;
import java.util.List;

public interface PostService {
    PostResponse addPost(String subjectId, PostRequest request) throws IOException;
    PostResponse getPostByPostId(String postId);
    PostResponse updatePost(String postId, PostRequest postRequest, String subjectId) throws IOException;
    void deletePost(String postId);
    PageResponse<?> getPostMyPost(int pageNo, int pageSize, String... sorts);
    PageResponse<?> getPostByUserId(String userId, int pageNo, int pageSize, String... sorts);
    PageResponse<?> getAllPosts(int pageNo, int pageSize, String... sorts);
    PageResponse<?> searchPostsByName(String keyword, int pageNo, int pageSize, String... sorts);
    PageResponse<?> getPostBySubjectId(String subjectId, int pageNo, int pageSize, String... sorts);
}
