package com.grabtutor.grabtutor.entity;

import com.grabtutor.grabtutor.enums.Role;
import com.grabtutor.grabtutor.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Set;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded  = true)
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(unique = true)
    String email;
    @Column(nullable = false)
    String password;
    String fullName;
    LocalDate dob;
    String phoneNumber;
    @Enumerated(EnumType.STRING)
    Role role;
    @Builder.Default
    boolean isActive = true;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    UserStatus userStatus = UserStatus.NORMAL;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Post> posts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Report> reportsSent = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Report> reportsReceived = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Review> reviewSent = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Review> reviewReceived = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tutorInfoId")
    TutorInfo tutorInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountBalanceId")
    AccountBalance accountBalance;

    @Builder.Default
    @ManyToMany(mappedBy = "users")
    List<ChatRoom> chatRooms = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Message> messages = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade =  CascadeType.ALL)
    List<TutorBid> tutorBids = new ArrayList<>();

    @ManyToMany(mappedBy = "enrolledUsers")
    Set<Course> enrolledCourses;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    List<VerificationRequest> verificationRequests = new ArrayList<>();;
}
