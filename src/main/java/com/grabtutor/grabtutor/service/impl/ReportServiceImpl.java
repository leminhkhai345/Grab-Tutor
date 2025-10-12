package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.ReportRequest;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.dto.response.ReportResponse;
import com.grabtutor.grabtutor.entity.Post;
import com.grabtutor.grabtutor.entity.Report;
import com.grabtutor.grabtutor.entity.User;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.ReportMapper;
import com.grabtutor.grabtutor.repository.PostRepository;
import com.grabtutor.grabtutor.repository.ReportRepository;
import com.grabtutor.grabtutor.repository.UserRepository;
import com.grabtutor.grabtutor.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ReportServiceImpl implements ReportService {
    ReportRepository reportRepository;
    ReportMapper reportMapper;
    UserRepository userRepository;
    PostRepository postRepository;

    @Override
    public ReportResponse createReport(ReportRequest request, String userId, String postId) {
        Report report = reportMapper.toReport(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        report.setUser(user);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));
        report.setPost(post);
        return reportMapper.toReportResponse(reportRepository.save(report));
    }

    @Override
    public ReportResponse getReportById(String id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REPORT_NOT_EXIST));
        if(report.isDeleted()){
            throw new AppException(ErrorCode.REPORT_NOT_EXIST);
        }
        return reportMapper.toReportResponse(report);
    }

    @Override
    public PageResponse<?> getReportByUserId(String userId, int pageNo, int pageSize, String... sorts) {
        List<Sort.Order> orders = new ArrayList<>();
        for(String sortBy : sorts){
            // firstname:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()){
                if(matcher.group(3).equalsIgnoreCase("desc")){
                    orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                }
            }

        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(orders));
        Page<Report> reports = reportRepository.findByUserIdAndIsDeletedFalse(userId, pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(reports.getTotalPages())
                .items(reports.stream().map(reportMapper::toReportResponse).toList())
                .build();
    }

    @Override
    public ReportResponse resolveReport(String id) {
        return null;
    }

    @Override
    public void deleteReport(String id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REPORT_NOT_EXIST));
        report.setDeleted(true);
        reportRepository.save(report);
    }
}
