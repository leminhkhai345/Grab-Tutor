package com.grabtutor.grabtutor.controller;


import com.grabtutor.grabtutor.dto.request.ReviewRequest;
import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewService reviewService;

    @PostMapping("/post/{postId}")
    public ApiResponse<?> createReview(@PathVariable String postId,
                                       @RequestBody @Valid ReviewRequest request){
        return  ApiResponse.builder()
                .message("Review created successfully")
                .data(reviewService.createReview(postId, request))
                .build();
    }

    @PutMapping("/{reviewId}")
    public ApiResponse<?> updateReview(@PathVariable String reviewId,
                                       @RequestBody @Valid ReviewRequest request){
        return  ApiResponse.builder()
                .message("Review updated successfully")
                .data(reviewService.updateReview(reviewId, request))
                .build();
    }

    @DeleteMapping("/{reviewId}")
    public ApiResponse<?> deleteReview(@PathVariable String reviewId){
        reviewService.deleteReview(reviewId);
        return  ApiResponse.builder()
                .message("Review deleted successfully")
                .build();
    }

    @GetMapping("/{reviewId}")
    public ApiResponse<?> getReviewById(@PathVariable String reviewId){
        return  ApiResponse.builder()
                .message("Review fetched successfully")
                .data(reviewService.getReviewById(reviewId))
                .build();
    }

    @GetMapping("/post/{postId}")
    public ApiResponse<?> getReviewsByPostId(@PathVariable String postId,
                                             @RequestParam(defaultValue = "0") int pageNo,
                                            @RequestParam(defaultValue = "10") int pageSize,
                                            @RequestParam(defaultValue = "createdAt|desc") String... sorts) {
        return ApiResponse.builder()
                .message("Reviews fetched successfully")
                .data(reviewService.getReviewsByPostId(postId, pageNo, pageSize, sorts))
                .build();
    }

    @GetMapping("/sender/{senderId}")
    public ApiResponse<?> getReviewsBySenderId(@PathVariable String senderId,
                                               @RequestParam(defaultValue = "0") int pageNo,
                                               @RequestParam(defaultValue = "10") int pageSize,
                                               @RequestParam(defaultValue = "createdAt|desc", required = false) String... sorts) {
        return ApiResponse.builder()
                .message("Reviews fetched successfully")
                .data(reviewService.getReviewsBySenderId(senderId, pageNo, pageSize, sorts))
                .build();
    }

    @GetMapping("/receiver/{receiverId}")
    public ApiResponse<?> getReviewsByReceiverId(@PathVariable String receiverId,
                                                 @RequestParam(defaultValue = "0") int pageNo,
                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                 @RequestParam(defaultValue = "createdAt|desc", required = false) String... sorts) {
        return ApiResponse.builder()
                .message("Reviews fetched successfully")
                .data(reviewService.getReviewsByReceiverId(receiverId, pageNo, pageSize, sorts))
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<?> getMyReviews(@RequestParam(defaultValue = "0") int pageNo,
                                       @RequestParam(defaultValue = "10") int pageSize,
                                       @RequestParam(defaultValue = "createdAt|desc", required = false) String... sorts) {
        return ApiResponse.builder()
                .message("My reviews fetched successfully")
                .data(reviewService.getMyReviews(pageNo, pageSize, sorts))
                .build();
    }
}
