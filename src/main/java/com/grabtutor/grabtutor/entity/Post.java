package com.grabtutor.grabtutor.entity;

import com.grabtutor.grabtutor.enums.PostStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
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

    @Builder.Default
    @Enumerated(EnumType.STRING)
    PostStatus status =  PostStatus.OPEN;
    @Builder.Default
    boolean isAccepted = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    Subject subject;

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Report> reports = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chatRoomId")
    ChatRoom chatRoom;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userTransactionId")
    UserTransaction userTransaction;

    @OneToMany(mappedBy = "post")
    @Builder.Default
    List<TutorBid> tutorBids = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    @Builder.Default
    List<VirtualTransaction> virtualTransactions = new ArrayList<>();
}
