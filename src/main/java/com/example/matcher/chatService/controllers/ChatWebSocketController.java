package com.example.matcher.chatService.controllers;

import com.example.matcher.chatService.dto.AcknowledgementDto;
import com.example.matcher.chatService.dto.message.NewMessageDTO;
import com.example.matcher.chatService.dto.message.DeleteMessageDTO;
import com.example.matcher.chatService.dto.message.EditMessageDTO;
import com.example.matcher.chatService.dto.message.ReadMessageDTO;
import com.example.matcher.chatService.model.Message;
import com.example.matcher.chatService.model.MessageStatus;
import com.example.matcher.chatService.service.ChatMessageService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class ChatWebSocketController {
    private ChatMessageService chatMessageService;

    @MessageMapping("/chat/message/send")
    public void sendMessage(@Payload NewMessageDTO newMessageDTO) {
        chatMessageService.sendNewMessage(newMessageDTO);
    }

    @MessageMapping("/chat/message/read")
    public void confirmMessage(ReadMessageDTO message) {
        chatMessageService.markMessageAsRead(message);
    }


    @MessageMapping("/chat/message/delete")
    public void deleteMessage(@Payload DeleteMessageDTO deleteMessageDTO) {
        chatMessageService.deleteMessage(deleteMessageDTO);
    }

    @MessageMapping("/chat/message/edit")
    public void editMessage(@Payload EditMessageDTO editMessageDTO) {
        chatMessageService.editMessage(editMessageDTO);
    }

}

