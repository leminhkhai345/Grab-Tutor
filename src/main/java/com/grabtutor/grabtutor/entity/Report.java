package com.grabtutor.grabtutor.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "reports")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Report extends BaseEntity {

    String detail;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    Post post;

}
