package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.response.*;
import com.grabtutor.grabtutor.enums.Role;

public interface StatisticService {
    PostStatusStatisticsResponse getPostStatusStatistics();
    ReviewStarStatisticResponse getReviewStarStatistics();
    UserTotalStatisticResponse getUserTotalStatistics();
    UserStatusStatistic userStatusStatistics(Role role);
    RevenueProfitResponse revenueProfitMonthStatistics(int year);
    ReportStatusResponse getReportStatusStatistics();
}
