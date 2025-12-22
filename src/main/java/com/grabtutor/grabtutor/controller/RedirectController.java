package com.grabtutor.grabtutor.controller;

import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vnpay")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RedirectController {
    VNPayService vnPayService;
    String port = "5174";

    @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request){
        vnPayService.transactionReturn(request);
        return "redirect:http://localhost:"+port;
    }
}
