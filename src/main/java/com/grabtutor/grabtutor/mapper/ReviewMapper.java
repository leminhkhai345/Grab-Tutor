package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.request.ReviewRequest;
import com.grabtutor.grabtutor.dto.response.ReviewResponse;
import com.grabtutor.grabtutor.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review toReview(ReviewRequest request);
    default ReviewResponse toReviewResponse(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewResponse.ReviewResponseBuilder reviewResponse = ReviewResponse.builder();

        reviewResponse.id( review.getId() );
        reviewResponse.createdAt( review.getCreatedAt() );
        reviewResponse.updatedAt( review.getUpdatedAt() );
        reviewResponse.isDeleted( review.isDeleted() );
        reviewResponse.stars( review.getStars() );
        reviewResponse.description( review.getDescription() );
        reviewResponse.senderId( review.getSender() != null ? review.getSender().getId() : null );
        reviewResponse.receiverId( review.getReceiver() != null ? review.getReceiver().getId() : null );
        reviewResponse.postId( review.getPost() != null ? review.getPost().getId() : null );

        return reviewResponse.build();
    }
    void updateReviewFromRequest(ReviewRequest request,@MappingTarget Review review);
}
