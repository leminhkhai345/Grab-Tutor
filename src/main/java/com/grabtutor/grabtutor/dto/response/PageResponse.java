package com.grabtutor.grabtutor.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PageResponse<T> {
    int pageNo;
    int pageSize;
    int totalPages;
    T items;
}
