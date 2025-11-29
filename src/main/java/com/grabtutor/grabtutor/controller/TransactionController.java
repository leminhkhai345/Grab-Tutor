package com.grabtutor.grabtutor.controller;

import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.service.TransactionService;
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
    TransactionService transactionService;

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
    @GetMapping("/myUserTransaction")
    public ApiResponse<?> getMyUserTransactions(@RequestParam(defaultValue = "0") int pageNo,
                                                @RequestParam(defaultValue = "10") int pageSize,
                                                @RequestParam(defaultValue = "createdAt:desc") String... sorts){
        return ApiResponse.builder()
                .message("get account user transactions successfully")
                .data(transactionService.getMyUserTransactions(pageNo, pageSize, sorts))
                .build();
    }
    @GetMapping("/userTransaction")
    public ApiResponse<?> getAllUserTransactions(@RequestParam(defaultValue = "0") int pageNo,
                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                 @RequestParam(defaultValue = "createdAt:desc") String... sorts){
        return ApiResponse.builder()
                .message("get all user transactions successfully")
                .data(transactionService.getAllUserTransactions(pageNo, pageSize, sorts))
                .build();
    }

}
