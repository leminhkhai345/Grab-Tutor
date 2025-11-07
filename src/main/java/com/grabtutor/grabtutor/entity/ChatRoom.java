package com.grabtutor.grabtutor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grabtutor.grabtutor.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
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
    boolean chatEnabled = true;
    @Builder.Default
    RoomStatus status = RoomStatus.IN_PROGRESS;
    @OneToOne(mappedBy = "chatRoom")
    @JsonIgnore
    Post post;
    // bên này là "bị ánh xạ"
    @ManyToMany
    @JoinTable(
            name = "user_group", // bảng trung gian
            inverseJoinColumns = @JoinColumn(name = "userId"), // khóa ngoại đến User
            joinColumns = @JoinColumn(name = "roomId") // khóa ngoại đến Group
    )
    @Builder.Default
    List<User> users = new ArrayList<>();
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    List<Message> messages = new ArrayList<>();
}
