package com.example.matcher.chatService.controllers;

import com.example.matcher.chatService.aspect.AspectAnnotation;
import com.example.matcher.chatService.dto.TypingMessageDTO;
import com.example.matcher.chatService.dto.message.*;
import com.example.matcher.chatService.service.ChatMessageService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class ChatWebSocketController {
    private ChatMessageService chatMessageService;
    @AspectAnnotation
    @MessageMapping("/chat/message/send")
    public void sendMessage(@Payload NewMessageDTO newMessageDTO) {
        chatMessageService.sendNewMessage(newMessageDTO);
    }

    @AspectAnnotation
    @MessageMapping("/chat/message/read")
    public void confirmMessage(ReadMessageDTO message) {
        chatMessageService.markMessageAsRead(message);
    }

    @AspectAnnotation
    @MessageMapping("/chat/message/delete")
    public void deleteMessage(@Payload DeleteMessageDTO deleteMessageDTO) {
        chatMessageService.deleteMessage(deleteMessageDTO);
    }

    @AspectAnnotation
    @MessageMapping("/chat/message/edit")
    public void editMessage(@Payload EditMessageDTO editMessageDTO) {
        chatMessageService.editMessage(editMessageDTO);
    }

    @AspectAnnotation
    @MessageMapping("/chat/typing")
    public void typingMessage(@Payload TypingMessageDTO typingMessageDTO) {
        chatMessageService.typingSignal(typingMessageDTO);
    }

}

