package com.grabtutor.grabtutor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class RevenueProfitResponse {
    List<RevenueProfitMonthlyResponse> monthly;
    double totalProfit;
    double totalRevenue;
}
