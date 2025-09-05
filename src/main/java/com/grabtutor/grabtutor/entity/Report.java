package com.grabtutor.grabtutor.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "reports")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    String id;
    String detail;
    LocalDate createdAt = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    Post post;

}
