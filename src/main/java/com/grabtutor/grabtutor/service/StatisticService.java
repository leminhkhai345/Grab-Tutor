package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.response.PostStatusStatisticsResponse;
import com.grabtutor.grabtutor.dto.response.ReviewStarStatisticResponse;

public interface StatisticService {
    PostStatusStatisticsResponse getPostStatusStatistics();
    ReviewStarStatisticResponse getReviewStarStatistics();
}
