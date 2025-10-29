package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.ReviewRequest;
import com.grabtutor.grabtutor.dto.response.ReviewResponse;
import com.grabtutor.grabtutor.entity.Post;
import com.grabtutor.grabtutor.entity.Review;
import com.grabtutor.grabtutor.entity.TutorBid;
import com.grabtutor.grabtutor.enums.BiddingStatus;
import com.grabtutor.grabtutor.enums.PostStatus;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.ReviewMapper;
import com.grabtutor.grabtutor.repository.PostRepository;
import com.grabtutor.grabtutor.repository.ReviewRepository;
import com.grabtutor.grabtutor.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ReviewServiceImpl implements ReviewService{
    ReviewRepository reviewRepository;
    ReviewMapper reviewMapper;
    PostRepository postRepository;

    @Override
    public ReviewResponse createReview(String postId, ReviewRequest reviewRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");
        Review review = reviewMapper.toReview(reviewRequest);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));
        if(post.isDeleted()){
            throw new AppException(ErrorCode.POST_NOT_EXIST);
        }
        if(!post.getStatus().equals(PostStatus.SOLVED))
        {
            throw new AppException(ErrorCode.POST_NOT_SOLVED);
        }
        if(!post.getUser().getId().equals(userId)){
            throw new AppException(ErrorCode.FORBIDDEN);
        }
         var tutorBid = post.getTutorBids().stream()
                .filter(bid -> bid.getStatus().equals(BiddingStatus.ACCEPTED))
                .findFirst();
        review.setSender(post.getUser());
        review.setPost(post);
        review.setReceiver(tutorBid.get().getUser());
        if(review.getReceiver().getId().equals(userId)){
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        return reviewMapper.toReviewResponse(reviewRepository.save(review));
    }

    @Override
    public ReviewResponse updateReview(String reviewId, ReviewRequest reviewRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXIST));
        if(!review.getSender().getId().equals(userId)){
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        reviewMapper.updateReviewFromRequest(reviewRequest, review);
        return reviewMapper.toReviewResponse(reviewRepository.save(review));
    }

    @Override
    public void deleteReview(String reviewId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXIST));
        if(review.isDeleted()){
            throw new AppException(ErrorCode.REVIEW_NOT_EXIST);
        }
        if(!review.getSender().getId().equals(userId)){
            throw new AppException(ErrorCode.FORBIDDEN);
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


    @Override
    public List<ReviewResponse> getReviewsByPostId(String postId) {
        var reviews = reviewRepository.findByPostIdAndIsDeletedFalse(postId);
        return reviews.stream().map(reviewMapper::toReviewResponse).toList();
    }

    @Override
    public List<ReviewResponse> getReviewsBySenderId(String userId) {
        var reviews = reviewRepository.findBySenderIdAndIsDeletedFalse(userId);
        return reviews.stream().map(reviewMapper::toReviewResponse).toList();
    }

    @Override
    public List<ReviewResponse> getReviewsByReceiverId(String userId) {
        var reviews = reviewRepository.findByReceiverIdAndIsDeletedFalse(userId);
        return reviews.stream().map(reviewMapper::toReviewResponse).toList();
    }

    @PreAuthorize("hasRole('TUTOR')")
    @Override
    public List<ReviewResponse> getMyReviews() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");
        var reviews = reviewRepository.findBySenderIdAndIsDeletedFalse(userId);
        return reviews.stream().map(reviewMapper::toReviewResponse).toList();
    }
}
