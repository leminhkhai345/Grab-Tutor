package com.grabtutor.grabtutor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class SubjectResponse {
    String id;
    String name;
    String description;
    boolean isDeleted;
    LocalDate createdAt;
    LocalDate updatedAt;
}
