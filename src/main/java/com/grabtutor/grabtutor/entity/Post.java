package com.grabtutor.grabtutor.entity;

import com.grabtutor.grabtutor.enums.PostStatus;
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Report> reports;

    @OneToOne
    @JoinColumn(name = "chatRoomId")
    ChatRoom chatRoom;

    @OneToOne
    @JoinColumn(name = "userTransactionId")
    UserTransaction userTransaction;

    @OneToMany(mappedBy = "post")
    List<TutorBid> tutorBids;
}
