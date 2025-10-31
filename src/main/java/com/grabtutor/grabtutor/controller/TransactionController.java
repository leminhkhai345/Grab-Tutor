package com.grabtutor.grabtutor.controller;

import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.service.impl.VNPayServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionController {
    VNPayServiceImpl vnPayService;

    @PostMapping
    public ApiResponse<?> addFund(@RequestParam("amount") int orderTotal,HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +"/grabtutor/transaction";

        String vnpayUrl = vnPayService.addFund(orderTotal, baseUrl);
        return ApiResponse.builder()
                .message("Start transaction successfully!")
                .data(vnpayUrl)
                .build();
    }
    @GetMapping("/vnpay-payment-return")
    public ApiResponse<?> paymentCompleted(HttpServletRequest request){
        return ApiResponse.builder()
                .message("Transaction completed")
                .data(vnPayService.transactionReturn(request))
                .build();
    }
}
