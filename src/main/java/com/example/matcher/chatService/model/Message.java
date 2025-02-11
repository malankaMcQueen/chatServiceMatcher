package com.example.matcher.chatService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Entity
@Table(name = "chat_messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID senderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    @JsonIgnore
    private ChatRoom chatRoom;

    private String content;

    private LocalDateTime timestamp;
    private MessageStatus status;

    @Column(name = "reply_to_message_id")
    private Long replyToMessageId;
}
