package com.grabtutor.grabtutor.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "message")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String content;
    boolean isDeleted;
    LocalDate createdAt = LocalDate.now();
    LocalDate updatedAt = LocalDate.now();
    @ManyToOne
    @JoinColumn(name = "senderId", nullable = false)
    User user;
}
