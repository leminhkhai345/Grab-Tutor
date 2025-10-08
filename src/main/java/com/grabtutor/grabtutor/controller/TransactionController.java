package com.grabtutor.grabtutor.controller;

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

    @PostMapping("/deposit")
    public String deposit(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder( orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }
//    @GetMapping("/vnpay-payment-return")
//    public DepositResponse paymentCompleted(HttpServletRequest request){
//        int paymentStatus = vnPayService.orderReturn(request);
//
//
//    }
}
