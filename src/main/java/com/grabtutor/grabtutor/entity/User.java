package com.grabtutor.grabtutor.entity;

import com.grabtutor.grabtutor.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(unique = true)
    String username;
    String password;
    LocalDate dob;
    String email;
    String phoneNumber;
    boolean isActive;
    UserStatus userStatus = UserStatus.NORMAL;
    LocalDate createdAt = LocalDate.now();
    LocalDate updatedAt = LocalDate.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Report> reports;
}
