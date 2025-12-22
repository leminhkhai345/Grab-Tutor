package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.response.TransactionResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {
    String addFund(int total, String urlReturn);
    void transactionReturn(HttpServletRequest request);
}
