package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.response.DepositResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {
    String createOrder(int total, String orderInfor, String urlReturn);
    DepositResponse orderReturn(HttpServletRequest request);
}
