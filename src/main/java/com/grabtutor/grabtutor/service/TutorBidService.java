package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.TutorBidRequest;
import com.grabtutor.grabtutor.dto.response.TutorBidResponse;

import java.util.List;

public interface TutorBidService {
    TutorBidResponse addTutorBid(TutorBidRequest request);
    List<TutorBidResponse> getAllTutorBid(String postId);
    void acceptTutor(String tutorBidId);
}
