package com.grabtutor.grabtutor.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "posts")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseEntity {

    String imageUrl;
    String description;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "sujectId", nullable = false)
    Subject subject;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Report> reports;
}
