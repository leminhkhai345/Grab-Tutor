package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.request.ReportRequest;
import com.grabtutor.grabtutor.dto.response.ReportResponse;
import com.grabtutor.grabtutor.entity.Report;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    Report toReport(ReportRequest request);
    ReportResponse toReportResponse(Report report);
}
