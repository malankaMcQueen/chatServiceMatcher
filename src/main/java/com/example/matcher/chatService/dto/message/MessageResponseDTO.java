package com.example.matcher.chatService.dto.message;

import com.example.matcher.chatService.model.Message;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MessageResponseDTO {
    private Long id;
    private UUID senderId;
    private String content;
    private LocalDateTime timestamp;
    private Long replyToMessageId;
    private String replyToMessageContent; // Текст ответа

    public static MessageResponseDTO fromEntity(Message message, Message replyMessage) {
        MessageResponseDTO response = MessageResponseDTO.builder()
                .id(message.getId())
                .senderId(message.getSenderId())
                .content(message.getContent())
                .timestamp(message.getTime())
                .replyToMessageId(message.getReplyToMessageId())
                .build();
//        response.setId(message.getId());
//        response.setSenderId(message.getSenderId());
//        response.setContent(message.getContent());
//        response.setTimestamp(message.getTime());
//        response.setReplyToMessageId(message.getReplyToMessageId());

        response.setReplyToMessageContent(replyMessage != null ? replyMessage.getContent() : null);

        return response;
    }
}

