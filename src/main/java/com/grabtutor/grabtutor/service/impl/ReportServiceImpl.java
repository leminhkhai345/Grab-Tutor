package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.ReportRequest;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.dto.response.ReportResponse;
import com.grabtutor.grabtutor.entity.*;
import com.grabtutor.grabtutor.enums.*;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.ReportMapper;
import com.grabtutor.grabtutor.repository.*;
import com.grabtutor.grabtutor.service.ReportService;
import com.grabtutor.grabtutor.socket.NotificationService;
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
    NotificationService notificationService;
    UserTransactionRepository userTransactionRepository;
    private final AccountBalanceRepository accountBalanceRepository;

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
        for(var tutorbid : tutorBid) {
            if(tutorbid.getStatus() == BiddingStatus.ACCEPTED) {
                report.setReceiver(tutorbid.getUser());
                break;
            }
        }

        report.setPost(post);
        report.setStatus(ReportStatus.PENDING);
        reportRepository.save(report);
        log.info("Report {} {} as been created", report.getId(), report.getDetail());

        chatRoomRepository.save(chatRoom);

        var admin = userRepository.findByEmail("admin").orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        notificationService.sendNotification(admin.getId(),"New Report!", "Report: " + report.getId(), report.getId());

        return reportMapper.toReportResponse(report);
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

        Post post = report.getPost();
        var reportList = post.getReports().stream()
                .filter(rp -> rp.getStatus().equals(ReportStatus.PENDING))
                .toList();
        if(accept) {
            for (Report rp : reportList) {
                rp.setStatus(ReportStatus.ACCEPTED);
            }
        }
        else {
            for (Report rp : reportList) {
                rp.setStatus(ReportStatus.REJECTED);
            }
        }

        ChatRoom chatRoom = post.getChatRoom();
        UserTransaction transaction = post.getUserTransaction();
        AccountBalance balance;

        if(accept) {
            post.setStatus(PostStatus.CLOSED);
            chatRoom.setStatus(RoomStatus.RESOLVED_REFUND);
            transaction.setStatus(TransactionStatus.FAILED);
            balance = report.getSender().getAccountBalance();
            balance.setBalance(balance.getBalance() + transaction.getAmount());
        }
        else {
            post.setStatus(PostStatus.SOLVED);
            chatRoom.setStatus(RoomStatus.RESOLVED_NORMAL);
            transaction.setStatus(TransactionStatus.SUCCESS);
            balance = report.getReceiver().getAccountBalance();
            balance.setBalance(balance.getBalance() + transaction.getAmount());

        }

        chatRoomRepository.save(chatRoom);
        postRepository.save(post);
        accountBalanceRepository.save(balance);

        notificationService.sendNotification(balance.getUser().getId()
                ,"Account balance"
                , "+"+post.getUserTransaction().getAmount()
                ,balance.getUser().getAccountBalance().getId());

        notificationService.sendSignal(chatRoom.getId(), MessageType.UPDATE
                , "ChatRoom resolved"
                , "");

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
