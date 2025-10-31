package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.TutorBidRequest;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.dto.response.TutorBidResponse;

import java.util.List;

public interface TutorBidService {
    TutorBidResponse addTutorBid(TutorBidRequest request);
    List<TutorBidResponse> getAllTutorBid(String postId);
    PageResponse<?> getMyTutorBid(int pageNo, int pageSize, String... sorts);
    void acceptTutor(String tutorBidId);
    void cancelTutorBid(String tutorBidId);
}
