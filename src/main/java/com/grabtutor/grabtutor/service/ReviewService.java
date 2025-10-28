package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.ReviewRequest;
import com.grabtutor.grabtutor.dto.response.ReviewResponse;
import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(String postId, ReviewRequest reviewRequest);
    ReviewResponse updateReview(String reviewId, ReviewRequest reviewRequest);
    void deleteReview(String reviewId);
    ReviewResponse getReviewById(String reviewId);
    List<ReviewResponse> getReviewsByPostId(String postId);
    List<ReviewResponse> getReviewsByUserId(String userId);
    List<ReviewResponse> getMyReviews();

}
