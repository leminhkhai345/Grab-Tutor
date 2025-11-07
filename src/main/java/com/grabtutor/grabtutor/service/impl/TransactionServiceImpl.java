package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.entity.User;
import com.grabtutor.grabtutor.entity.UserTransaction;
import com.grabtutor.grabtutor.entity.VirtualTransaction;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.UserTransactionMapper;
import com.grabtutor.grabtutor.repository.UserRepository;
import com.grabtutor.grabtutor.repository.UserTransactionRepository;
import com.grabtutor.grabtutor.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class TransactionServiceImpl implements TransactionService {
    UserRepository userRepository;
    UserTransactionRepository userTransactionRepository;
    UserTransactionMapper userTransactionMapper;

    @Override
    public PageResponse<?> getMyUserTransactions(int pageNo, int pageSize, String... sorts) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        String accountBalanceId = user.getAccountBalance().getId();
        List<Sort.Order> orders = new ArrayList<>();
        for(String sortBy : sorts) {
            // firstname:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(orders));
        Page<UserTransaction> transactions = userTransactionRepository
                .findAllBySenderIdOrReceiverId(accountBalanceId, accountBalanceId, pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(transactions.getTotalPages())
                .items(transactions.stream().map(userTransactionMapper::toUserTransactionResponse).toList())
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<?> getAllUserTransactions(int pageNo, int pageSize, String... sorts) {
        List<Sort.Order> orders = new ArrayList<>();
        for(String sortBy : sorts) {
            // firstname:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(orders));
        Page<UserTransaction> transactions = userTransactionRepository
                .findAll(pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(transactions.getTotalPages())
                .items(transactions.stream().map(userTransactionMapper::toUserTransactionResponse).toList())
                .build();
    }
}
