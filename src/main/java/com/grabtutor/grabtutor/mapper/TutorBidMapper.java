package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.request.TutorBidRequest;
import com.grabtutor.grabtutor.dto.response.TutorBidResponse;
import com.grabtutor.grabtutor.entity.TutorBid;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TutorBidMapper {
    TutorBidResponse toTutorBidResponse(TutorBid tutorBid);
    TutorBid toTutorBid(TutorBidRequest request);
}
