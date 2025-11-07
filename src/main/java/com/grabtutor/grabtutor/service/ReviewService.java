package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.ReviewRequest;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.dto.response.ReviewResponse;
import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(String postId, ReviewRequest reviewRequest);
    ReviewResponse updateReview(String reviewId, ReviewRequest reviewRequest);
    void deleteReview(String reviewId);
    ReviewResponse getReviewById(String reviewId);
    PageResponse<?> getReviewsByPostId(String postId, int pageNo, int pageSize, String... sorts);
    PageResponse<?> getReviewsBySenderId(String sender, int pageNo, int pageSize, String... sorts);
    PageResponse<?> getReviewsByReceiverId(String receiverId, int pageNo, int pageSize, String... sorts);
    PageResponse<?> getMyReviews(int pageNo, int pageSize, String... sorts);

}
