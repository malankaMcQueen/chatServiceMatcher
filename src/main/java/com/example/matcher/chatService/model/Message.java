package com.example.matcher.chatService.model;

import com.example.matcher.chatService.model.converter.MessageStatusConverter;
import com.example.matcher.chatService.model.converter.MessageTypeConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Entity
@Table(schema = "chat", name = "messages")
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

    @Column(name = "type")
    @Convert(converter = MessageTypeConverter.class)
    private MessageType messageType;

    private LocalDateTime time;

    @Convert(converter = MessageStatusConverter.class)
    private MessageStatus status;

    @Column(name = "reply_to_message_id")
    private Long replyToMessageId;
}
