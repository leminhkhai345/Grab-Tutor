package com.grabtutor.grabtutor.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "messages")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message extends BaseEntity{
    String message;

    @ManyToOne
    @JoinColumn(name = "senderId", nullable = false)
    User user;
    @ManyToOne
    @JoinColumn(name = "roomId", nullable = false)
    ChatRoom chatRoom;
}
