package com.grabtutor.grabtutor.entity;

import com.grabtutor.grabtutor.enums.RoomStatus;
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

    @Builder.Default
    boolean isSubmitted = false;
    @Builder.Default
    boolean chatEnabled = true;
    @Builder.Default
    RoomStatus status = RoomStatus.IN_PROGRESS;
    @OneToOne(mappedBy = "chatRoom")
    Post post;
    // bên này là "bị ánh xạ"
    @ManyToMany
    @JoinTable(
            name = "user_group", // bảng trung gian
            inverseJoinColumns = @JoinColumn(name = "userId"), // khóa ngoại đến User
            joinColumns = @JoinColumn(name = "roomId") // khóa ngoại đến Group
    )
    List<User> users;
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Message> messages;
}
