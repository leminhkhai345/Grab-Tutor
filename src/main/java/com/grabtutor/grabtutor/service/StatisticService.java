package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.response.PostStatusStatisticsResponse;
import com.grabtutor.grabtutor.dto.response.ReviewStarStatisticResponse;
import com.grabtutor.grabtutor.dto.response.UserStatusStatistic;
import com.grabtutor.grabtutor.dto.response.UserTotalStatisticResponse;
import com.grabtutor.grabtutor.enums.Role;

public interface StatisticService {
    PostStatusStatisticsResponse getPostStatusStatistics();
    ReviewStarStatisticResponse getReviewStarStatistics();
    UserTotalStatisticResponse getUserTotalStatistics();
    UserStatusStatistic userStatusStatistics(Role role);
}
