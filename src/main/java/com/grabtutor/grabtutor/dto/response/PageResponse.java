package com.grabtutor.grabtutor.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PageResponse<T> {
    int pageNo;
    int pageSize;
    int totalPages;
    T items;
}
