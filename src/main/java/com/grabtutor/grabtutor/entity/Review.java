package com.grabtutor.grabtutor.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "reviews")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Review extends BaseEntity {
    int stars;
    String description;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    Post post;

}
