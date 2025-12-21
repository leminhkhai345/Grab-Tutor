package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.response.*;
import com.grabtutor.grabtutor.entity.Post;
import com.grabtutor.grabtutor.enums.Role;
import com.grabtutor.grabtutor.enums.TransactionType;
import com.grabtutor.grabtutor.repository.*;
import com.grabtutor.grabtutor.service.StatisticService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticServiceImpl implements StatisticService {
    UserRepository userRepository;
    PostRepository postRepository;
    ReviewRepository reviewRepository;
    VirtualTransactionRepository virtualTransactionRepository;
    ReportRepository reportRepository;

    double revenueRate = 0.3;
    double operationFeeRate = 0.2;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public PostStatusStatisticsResponse getPostStatusStatistics(){
        var posts = postRepository.findAll();
        int open = 0;
        int closed = 0;
        int solved = 0;
        int reported = 0;
        for(Post post : posts){
            switch(post.getStatus()){
                case OPEN -> open++;
                case CLOSED -> closed++;
                case SOLVED -> solved++;
                case REPORTED -> reported++;
            }
        }
        return PostStatusStatisticsResponse.builder()
                .OPEN(open)
                .CLOSED(closed)
                .SOLVED(solved)
                .REPORTED(reported)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ReviewStarStatisticResponse getReviewStarStatistics(){
        var reviews = reviewRepository.findAll();
        int one = 0;
        int two = 0;
        int three = 0;
        int four = 0;
        int five = 0;
        for(var review : reviews){
            switch(review.getStars()){
                case 1 -> one++;
                case 2 -> two++;
                case 3 -> three++;
                case 4 -> four++;
                case 5 -> five++;
            }
        }
        return ReviewStarStatisticResponse.builder()
                .one(one)
                .two(two)
                .three(three)
                .four(four)
                .five(five)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public UserTotalStatisticResponse getUserTotalStatistics(){
        var users = userRepository.findAllByIsDeletedFalseAndIsActiveTrue();
        int totalUsers = users.size();
        int tutors = 0, students = 0, admins = 0;
        for(var user : users){
            switch(user.getRole()){
                case TUTOR -> tutors++;
                case USER -> students++;
                case ADMIN -> admins++;
            }
        }
        return UserTotalStatisticResponse.builder()
                .totalUsers(totalUsers)
                .tutors(tutors)
                .students(students)
                .admins(admins)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public UserStatusStatistic userStatusStatistics(Role role){
        var users = userRepository.findAll();
        int total = 0;
        int pending = 0, normal = 0, warning = 0, banned = 0;
        for(var user : users){
            if(user.getRole().equals(role)){
                total++;
                switch (user.getUserStatus()){
                    case PENDING -> pending++;
                    case NORMAL -> normal++;
                    case WARNING -> warning++;
                    case BANNED -> banned++;
                }
            }
        }
        return UserStatusStatistic.builder()
                .total(total)
                .PENDING(pending)
                .NORMAL(normal)
                .WARNING(warning)
                .BANNED(banned)
                .build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public RevenueProfitResponse revenueProfitMonthStatistics(int year){
        List<RevenueProfitMonthlyResponse> revenueProfitMonthlyResponseList = new ArrayList<>();

        LocalDateTime start = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime end = start.plusYears(1);
        Map<Integer, Double> map = new HashMap<>();

        for (Object[] row : virtualTransactionRepository.sumByMonth(start, end, TransactionType.ADD_FUND.name())) {
            int m = ((Number) row[0]).intValue();     // 1 -> 12
            Double total = (Double) row[1];
            map.put(m, total);
        }

        List<RevenueProfitMonthlyResponse> result = new ArrayList<>();
//        map.getOrDefault(m, 0.0)
        for (int m = 1; m <= 12; m++) {
            result.add( RevenueProfitMonthlyResponse.builder()
                    .month(m)
                    .revenue(map.getOrDefault(m, 0.0)*revenueRate)
                    .profit(map.getOrDefault(m, 0.0)*revenueRate*operationFeeRate)
                    .build());
        }
        Double totalRevenue = result.stream().mapToDouble(RevenueProfitMonthlyResponse::getRevenue).sum();
        Double totalProfit = result.stream().mapToDouble(RevenueProfitMonthlyResponse::getProfit).sum();

        return RevenueProfitResponse.builder()
                .monthly(result)
                .totalProfit(totalProfit)
                .totalRevenue(totalRevenue)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ReportStatusResponse getReportStatusStatistics() {
        var reports = reportRepository.findAll();
        int totalReports = 0;
        int pending = 0;
        int rejected = 0;
        int accepted = 0;
        for (var report : reports) {
            totalReports++;
            switch (report.getStatus()) {
                case PENDING -> pending++;
                case REJECTED -> rejected++;
                case ACCEPTED -> accepted++;
            }
        }
        return ReportStatusResponse.builder()
                .totalReports(totalReports)
                .pending(pending)
                .rejected(rejected)
                .accepted(accepted)
                .build();
    }
}
