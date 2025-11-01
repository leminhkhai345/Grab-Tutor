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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Post> posts;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Report> reportsSent;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Report> reportsReceived;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Review> reviewSent;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Review> reviewReceived;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tutorInfoId")
    TutorInfo tutorInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountBalanceId", nullable = false)
    AccountBalance accountBalance;

    @ManyToMany(mappedBy = "users")
    private List<ChatRoom> chatRooms;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<VirtualTransaction> virtualTransactions;

    @OneToMany(mappedBy = "user", cascade =  CascadeType.ALL)
    List<TutorBid> tutorBids;

    @ManyToMany(mappedBy = "enrolledUsers")
    Set<Course> enrolledCourses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Notification> notifications;
}
