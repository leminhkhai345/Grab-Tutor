package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.ReviewRequest;
import com.grabtutor.grabtutor.dto.response.ReviewResponse;
import com.grabtutor.grabtutor.entity.Post;
import com.grabtutor.grabtutor.entity.Review;
import com.grabtutor.grabtutor.entity.User;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.ReviewMapper;
import com.grabtutor.grabtutor.repository.PostRepository;
import com.grabtutor.grabtutor.repository.ReviewRepository;
import com.grabtutor.grabtutor.repository.UserRepository;
import com.grabtutor.grabtutor.service.ReviewService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@Data
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ReviewServiceImpl implements ReviewService{
    ReviewRepository reviewRepository;
    ReviewMapper reviewMapper;
    UserRepository userRepository;
    PostRepository postRepository;

    @Override
    public ReviewResponse createReview(String userId, String postId, ReviewRequest reviewRequest) {
        Review review = reviewMapper.toReview(reviewRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));
        review.setUser(user);
        review.setPost(post);
        return reviewMapper.toReviewResponse(reviewRepository.save(review));
    }

    @Override
    public ReviewResponse updateReview(String userId, String reviewId, ReviewRequest reviewRequest) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXIST));
        if(!review.getUser().getId().equals(userId)){
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        reviewMapper.updateReviewFromRequest(reviewRequest, review);
        return reviewMapper.toReviewResponse(reviewRepository.save(review));
    }

    @Override
    public void deleteReview(String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXIST));
        if(review.isDeleted()){
            throw new AppException(ErrorCode.REVIEW_NOT_EXIST);
        }
        review.setDeleted(true);
        reviewRepository.save(review);
    }

    @Override
    public ReviewResponse getReviewById(String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXIST));
        if(review.isDeleted()){
            throw new AppException(ErrorCode.REVIEW_NOT_EXIST);
        }
        return reviewMapper.toReviewResponse(review);
    }
}
