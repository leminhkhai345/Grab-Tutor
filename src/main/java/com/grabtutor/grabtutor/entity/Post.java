package com.grabtutor.grabtutor.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "posts")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String imageUrl;
    String description;
    boolean isDeleted = false;
    LocalDate createdAt = LocalDate.now();
    LocalDate updatedAt = LocalDate.now();
}
