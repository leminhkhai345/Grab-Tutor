package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.ReportRequest;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.dto.response.ReportResponse;
import com.grabtutor.grabtutor.entity.ChatRoom;
import com.grabtutor.grabtutor.entity.Post;
import com.grabtutor.grabtutor.entity.Report;
import com.grabtutor.grabtutor.entity.User;
import com.grabtutor.grabtutor.enums.BiddingStatus;
import com.grabtutor.grabtutor.enums.PostStatus;
import com.grabtutor.grabtutor.enums.ReportStatus;
import com.grabtutor.grabtutor.enums.RoomStatus;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.ReportMapper;
import com.grabtutor.grabtutor.repository.ChatRoomRepository;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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
    ChatRoomRepository chatRoomRepository;

    @PreAuthorize("hasRole('USER')")
    @Override
    public ReportResponse createReport(ReportRequest request, String postId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");
        Report report = reportMapper.toReport(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));
        ChatRoom chatRoom = post.getChatRoom();
        chatRoom.setStatus(RoomStatus.DISPUTED);

        post.setStatus(PostStatus.REPORTED);
        report.setSender(user);
        var tutorBid = post.getTutorBids();
        for(var tutor : tutorBid) {
            if(tutor.getStatus() == BiddingStatus.ACCEPTED) {
                report.setReceiver(tutor.getUser());
                break;
            }
        }

        report.setPost(post);
        report.setStatus(ReportStatus.PENDING);

        chatRoomRepository.save(chatRoom);
        return reportMapper.toReportResponse(reportRepository.save(report));
    }

    @Override
    public ReportResponse getReportById(String id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REPORT_NOT_EXIST));
        if (report.isDeleted()) {
            throw new AppException(ErrorCode.REPORT_NOT_EXIST);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!report.getReceiver().getId().equals(userId) && !report.getSender().getId().equals(userId)
                && !user.getRole().name().equals("ADMIN")) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        return reportMapper.toReportResponse(report);
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('TUTOR')")
    @Override
    public PageResponse<?> getReportByReceiverId(String receiverId, int pageNo, int pageSize, String... sorts) {
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
        Page<Report> reports = reportRepository.findByReceiverIdAndIsDeletedFalse(receiverId, pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(reports.getTotalPages())
                .items(reports.stream().map(reportMapper::toReportResponse).toList())
                .build();
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Override
    public PageResponse<?> getReportBySenderId(String senderId, int pageNo, int pageSize, String... sorts) {
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
        Page<Report> reports = reportRepository.findBySenderIdAndIsDeletedFalse(senderId, pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(reports.getTotalPages())
                .items(reports.stream().map(reportMapper::toReportResponse).toList())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ReportResponse resolveReport(String reportId, boolean accept) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new AppException(ErrorCode.REPORT_NOT_EXIST));
        if(report.isDeleted()){
            throw new AppException(ErrorCode.REPORT_NOT_EXIST);
        }
        if(report.getStatus() != ReportStatus.PENDING){
            throw new AppException(ErrorCode.REPORT_ALREADY_RESOLVED);
        }
        if(accept) {
            report.setStatus(ReportStatus.ACCEPTED);
        }
        else {
            report.setStatus(ReportStatus.REJECTED);
        }
        reportRepository.save(report);
        Post post = report.getPost();
        ChatRoom chatRoom = post.getChatRoom();
        if(accept) {
            post.setStatus(PostStatus.CLOSED);
            chatRoom.setStatus(RoomStatus.RESOLVED_REFUND);
        }
        else {
            post.setStatus(PostStatus.SOLVED);
            chatRoom.setStatus(RoomStatus.RESOLVED_NORMAL);
        }
        chatRoomRepository.save(chatRoom);
        postRepository.save(post);
        return reportMapper.toReportResponse(report);
    }
    @PreAuthorize("hasRole('USER')")
    @Override
    public void deleteReport(String reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new AppException(ErrorCode.REPORT_NOT_EXIST));
        if (report.isDeleted()) {
            throw new AppException(ErrorCode.REPORT_NOT_EXIST);
        }
        if(report.getStatus() != ReportStatus.PENDING){
            throw new AppException(ErrorCode.REPORT_ALREADY_RESOLVED);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");
        if (!report.getSender().getId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        report.setDeleted(true);
        reportRepository.save(report);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public PageResponse<?> getAllReports(int pageNo, int pageSize, String... sorts) {
        List<Sort.Order> orders = new ArrayList<>();
        for(String sortBy : sorts){
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
        Page<Report> reports = reportRepository.findAll(pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(reports.getTotalPages())
                .items(reports.stream().map(reportMapper::toReportResponse).toList())
                .build();
    }
}
