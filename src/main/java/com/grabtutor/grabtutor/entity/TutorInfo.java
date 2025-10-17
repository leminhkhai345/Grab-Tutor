package com.grabtutor.grabtutor.entity;

import com.grabtutor.grabtutor.dto.request.UserRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "tutor_info")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
public class TutorInfo extends BaseEntity{
    @Column(unique = true)
    String nationalId;
    String university;
    String highestAcademicDegree;
    String major;
    double averageStars;
    @OneToOne(mappedBy = "tutorInfo")
    User user;
}
