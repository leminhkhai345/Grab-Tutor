package com.grabtutor.grabtutor.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TutorInfoResponse {
    String userId;
    String nationalId;
    String university;
    String highestAcademicDegree;
    String major;
    double averageStars;
    int problemSolved;
    String fullName;
    String email;
    String phoneNumber;

}
