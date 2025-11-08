package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.request.ReportRequest;
import com.grabtutor.grabtutor.dto.response.ReportResponse;
import com.grabtutor.grabtutor.entity.Report;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    Report toReport(ReportRequest request);
    default ReportResponse toReportResponse(Report report){
        if (report == null) {
            return null;
        }
        return ReportResponse.builder()
                .id(report.getId())
                .reportStatus(report.getStatus().name())
                .detail(report.getDetail())
                .senderId(report.getSender() != null ? report.getSender().getId() : null)
                .receiverId(report.getReceiver() != null ? report.getReceiver().getId() : null)
                .postId(report.getPost() != null ? report.getPost().getId() : null)
                .chatRoomId(report.getPost().getChatRoom() != null ? report.getPost().getChatRoom().getId() : null)
                .createdAt(report.getCreatedAt())
                .updatedAt(report.getUpdatedAt())
                .isDeleted(report.isDeleted())
                .build();
    }
}
