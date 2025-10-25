package com.grabtutor.grabtutor.entity;

import com.grabtutor.grabtutor.enums.BiddingStatus;
import com.grabtutor.grabtutor.enums.QuestionLevel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tutor_bids")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TutorBid extends BaseEntity{
    @Builder.Default
    double proposedPrice = 0;
    @Builder.Default
    QuestionLevel questionLevel = QuestionLevel.EASY;
    String description;
    @Builder.Default
    BiddingStatus status =  BiddingStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "senderId")
    User user;

    @ManyToOne
    @JoinColumn(name = "postId")
    Post post;
}
