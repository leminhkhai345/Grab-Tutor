package com.grabtutor.grabtutor.controller;

import com.grabtutor.grabtutor.dto.request.PostRequest;
import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @PostMapping()
    public ApiResponse<?> createPost(@RequestBody @Valid PostRequest postRequest){
        return ApiResponse.builder()
                .data(postService.addPost(postRequest))
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
