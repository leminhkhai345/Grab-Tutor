package com.grabtutor.grabtutor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grabtutor.grabtutor.dto.request.PostRequest;
import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> createPost(
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file,
            @RequestParam("subjectId") String subjectId,
            @AuthenticationPrincipal Jwt jwt) throws IOException {

        String userId = jwt.getClaimAsString("userId");

        return ApiResponse.builder()
                .data(postService.addPost(userId, subjectId, description, file))
                .message("Post created successfully")
                .build();
    }

    @PostMapping("/{postId}")
    public ApiResponse<?> updatePost(@PathVariable String postId, @RequestBody @Valid PostRequest postRequest){
        return ApiResponse.builder()
                .data(postService.updatePost(postId, postRequest))
                .message("Post updated successfully")
                .build();
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<?> deletePost(@PathVariable String postId) {
        postService.deletePost(postId);
        return ApiResponse.builder()
                .message("Post deleted successfully")
                .build();
    }

    @GetMapping("/{postId}")
    public ApiResponse<?> getPostById(@PathVariable String postId) {
        return ApiResponse.builder()
                .message("get post by postid")
                .data(postService.getPostByPostId(postId))
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<?> getPostsByUserId(@PathVariable String userId) {
        return ApiResponse.builder()
                .message("get posts by userid")
                .data(postService.getPostByUserId(userId))
                .build();
    }

    @GetMapping("/list")
    public ApiResponse<?> getALlPosts(@RequestParam(defaultValue = "0") int pageNo,
        @RequestParam(defaultValue = "10") int pageSize){
        return ApiResponse.builder()
                .message("get all posts")
                .data(postService.getAllPosts(pageNo, pageSize))
                .build();
        }

}
