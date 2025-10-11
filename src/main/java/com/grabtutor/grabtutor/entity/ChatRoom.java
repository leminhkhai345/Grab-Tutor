package com.grabtutor.grabtutor.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "chatRooms")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom extends BaseEntity{
    @ManyToMany(mappedBy = "chatRooms") // bên này là "bị ánh xạ"
    private List<User> users;
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages;
}
