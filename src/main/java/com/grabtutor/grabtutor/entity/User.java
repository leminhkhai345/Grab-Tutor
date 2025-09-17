package com.grabtutor.grabtutor.entity;

import com.grabtutor.grabtutor.enums.Role;
import com.grabtutor.grabtutor.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
public class User extends BaseEntity {


    @Column(unique = true)
    String email;
    @Column(nullable = false)
    String password;
    LocalDate dob;
    String phoneNumber;
    @Enumerated(EnumType.STRING)
    Role role;
    @Builder.Default
    boolean isActive = false;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    UserStatus userStatus = UserStatus.NORMAL;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Report> reports;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tutor_info_id")
    TutorInfo tutorInfo;

}
