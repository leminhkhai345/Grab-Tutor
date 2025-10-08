package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.ReviewRequest;
import com.grabtutor.grabtutor.dto.response.ReviewResponse;

public interface ReviewService {
    ReviewResponse createReview(String userId, String postId, ReviewRequest reviewRequest);
    ReviewResponse updateReview(String userId, String reviewId, ReviewRequest reviewRequest);
    void deleteReview(String reviewId);
    ReviewResponse getReviewById(String reviewId);

}
