package com.example.matcher.chatService.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chat_rooms", indexes = {
        @Index(name = "idx_first_user", columnList = "firstUserId"),
        @Index(name = "idx_second_user", columnList = "secondUserId")
})
public class ChatRoom {
    @Id
    @GeneratedValue
    private Long id;
    private UUID firstUserId;
    private UUID secondUserId;
    private LocalDateTime timeLastUpdate;
    private LocalDateTime timeCreated;

    @JsonIgnore
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    List<Message> messageList;
}
