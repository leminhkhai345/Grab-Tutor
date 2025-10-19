package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.ReportRequest;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.dto.response.ReportResponse;

public interface ReportService {
    ReportResponse createReport(ReportRequest request,String postId);
    ReportResponse getReportById(String id);
    PageResponse<?> getReportByReceiverId(String receiverId, int pageNo, int pageSize, String... sortBy);
    PageResponse<?> getReportBySenderId(String senderId, int pageNo, int pageSize, String... sortBy);
    ReportResponse resolveReport(String id);
    void deleteReport(String id);

}
