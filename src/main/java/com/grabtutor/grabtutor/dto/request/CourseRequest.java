package com.grabtutor.grabtutor.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
public class CourseRequest {
    String name;
    String description;
    double price;
    String imageUrl; // anh dai dien khoa hoc
}
