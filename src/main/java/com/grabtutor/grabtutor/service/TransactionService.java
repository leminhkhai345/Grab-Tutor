package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.response.PageResponse;

public interface TransactionService {
    PageResponse<?> getMyUserTransactions(int pageNo, int pageSize, String... sorts);
    PageResponse<?> getAllUserTransactions(int pageNo, int pageSize, String... sorts);
}
