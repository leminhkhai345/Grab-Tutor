package com.grabtutor.grabtutor.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "tutor_info")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded  = true)
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class TutorInfo extends BaseEntity{
    @Column(unique = true)
    String nationalId;
    String university;
    String highestAcademicDegree;
    String major;
    @Builder.Default
    double averageStars = 0.0;
    @Builder.Default
    int problemSolved = 0;
    @OneToOne(mappedBy = "tutorInfo", cascade = CascadeType.ALL)
    User user;
}
