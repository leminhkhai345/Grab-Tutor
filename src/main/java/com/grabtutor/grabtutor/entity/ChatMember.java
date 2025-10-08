package com.grabtutor.grabtutor.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "chatMembers")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMember extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "chatBoxId", nullable = false)
    ChatRoom chatBox;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    User user;
}
