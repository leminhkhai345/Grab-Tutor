package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.AcceptPostRequest;
import com.grabtutor.grabtutor.dto.request.PostRequest;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.dto.response.PostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    PostResponse addPost(String subjectId, PostRequest request) throws IOException;
    PostResponse getPostByPostId(String postId);
    PostResponse updatePost(String postId, PostRequest postRequest, String subjectId) throws IOException;
    void deletePost(String postId);
    List<PostResponse> getPostByUserId(String userId);
    PageResponse<?> getAllPosts(int pageNo, int pageSize, String... sorts);
    void acceptPost(AcceptPostRequest request);

}
