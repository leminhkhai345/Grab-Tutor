package com.grabtutor.grabtutor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grabtutor.grabtutor.dto.request.PostRequest;
import com.grabtutor.grabtutor.dto.request.TutorBidRequest;
import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.service.FileUploadService;
import com.grabtutor.grabtutor.service.PostService;
import com.grabtutor.grabtutor.service.impl.TutorBidServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
    TutorBidServiceImpl tutorBidService;
    ObjectMapper objectMapper;
    FileUploadService fileUploadService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> createPost(
            @RequestParam("post") String postJson,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("subjectId") String subjectId) throws IOException {
        PostRequest request = objectMapper.readValue(postJson, PostRequest.class);
        String imageUrl;
        if (file != null && !file.isEmpty()) {
            imageUrl = fileUploadService.uploadFile(file);
            request.setImageUrl(imageUrl);
        }

        return ApiResponse.builder()
                .data(postService.addPost(subjectId, request))
                .message("Post created successfully")
                .build();
    }

    @PutMapping("/{postId}")
    public ApiResponse<?> updatePost(@PathVariable String postId,
                                     @RequestParam("post") String postJson,
                                     @RequestParam(value = "file", required = false) MultipartFile file,
                                     @RequestParam(value = "subjectId", required = false) String subjectId)
            throws IOException {

        PostRequest postRequest = objectMapper.readValue(postJson, PostRequest.class);
        String imageUrl;
        if (file != null && !file.isEmpty()) {
             imageUrl = fileUploadService.uploadFile(file);
             postRequest.setImageUrl(imageUrl);
        }

        return ApiResponse.builder()
                .data(postService.updatePost(postId, postRequest, subjectId))
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
                .message("get post by postId")
                .data(postService.getPostByPostId(postId))
                .build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<?> getPostsByUserId(@PathVariable String userId) {
        return ApiResponse.builder()
                .message("get posts by userid")
                .data(postService.getPostByUserId(userId))
                .build();
    }

    @GetMapping("/myPosts")
    public ApiResponse<?> getMyPosts(){
        return ApiResponse.builder()
                .message("get my posts")
                .data(postService.getPostMyPost())
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<?> getALlPosts(@RequestParam(defaultValue = "0") int pageNo,
                                      @RequestParam(defaultValue = "10") int pageSize,
                                      @RequestParam String... sorts){
        return ApiResponse.builder()
                .message("get all posts")
                .data(postService.getAllPosts(pageNo, pageSize, sorts))
                .build();
        }
    @PutMapping("/acceptTutor")
    public ApiResponse<?> acceptTutor(@RequestParam String tutorBidId){
        tutorBidService.acceptTutor(tutorBidId);
        return ApiResponse.builder()
                .message("accept post successfully")
                .build();
    }
    @PostMapping("/tutorBid")
    public ApiResponse<?> addTutorBid(@RequestBody TutorBidRequest request){
        return ApiResponse.builder()
                .data(tutorBidService.addTutorBid(request))
                .message("add tutor bid successfully")
                .build();
    }
    @GetMapping("/tutorBid")
    public ApiResponse<?> getAllTutorBid(@RequestParam String postId){
        return ApiResponse.builder()
                .data(tutorBidService.getAllTutorBid(postId))
                .message("get all tutor bid successfully")
                .build();
    }
    @GetMapping("/myTutorBids")
    public ApiResponse<?> getMyTutorBids(@RequestParam(defaultValue = "0") int pageNo,
                                         @RequestParam(defaultValue = "10") int pageSize,
                                         @RequestParam String... sorts){
        return ApiResponse.builder()
                .message("Get all my tutor bids successfully")
                .data(tutorBidService.getMyTutorBid(pageNo,pageSize, sorts))
                .build();
    }
    @PutMapping("/tutorBid")
    public ApiResponse<?> cancelTutorBid(@RequestParam String tutorBidId){
        tutorBidService.cancelTutorBid(tutorBidId);
        return ApiResponse.builder()
                .message("Cancel tutor bid successfully")
                .build();
    }
}
