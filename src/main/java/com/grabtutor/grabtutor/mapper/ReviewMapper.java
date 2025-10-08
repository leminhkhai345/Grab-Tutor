package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.request.ReviewRequest;
import com.grabtutor.grabtutor.dto.response.ReviewResponse;
import com.grabtutor.grabtutor.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review toReview(ReviewRequest request);
    ReviewResponse toReviewResponse(Review review);
    void updateReviewFromRequest(ReviewRequest request,@MappingTarget Review review);
}
