package com.grabtutor.grabtutor.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TutorInfoResponse {
    String userId;
    String nationalId;
    String university;
    String highestAcademicDegree;
    String major;
}
