package com.grabtutor.grabtutor.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "chatmember")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMember {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String chatBoxId;
    String userId;
    @ManyToOne
    @JoinColumn(name = "chatBoxId", nullable = false)
    ChatBox chatBox;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    User user;
}
