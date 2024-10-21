package com.example.matcher.chatService.controllers;

import com.example.matcher.chatService.aspect.AspectAnnotation;
import com.example.matcher.chatService.configuration.WebSocketConfiguration;
import com.example.matcher.chatService.dto.ChatRoomDTO;
import com.example.matcher.chatService.dto.NewMessageDTO;
import com.example.matcher.chatService.model.ChatRoom;
import com.example.matcher.chatService.model.Message;
import com.example.matcher.chatService.service.ChatMessageService;
import com.example.matcher.chatService.service.ChatRoomService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class ChatController {

    private ChatMessageService chatMessageService;
    private ChatRoomService chatRoomService;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfiguration.class);

    @GetMapping("/getRandomUUID")
    public ResponseEntity<UUID> getRandomUUid() {
        return new ResponseEntity<>(UUID.randomUUID(), HttpStatus.CREATED);
    }

    @GetMapping("/getAllChat")
    public ResponseEntity<List<ChatRoom>> getAllChatRoom() {
        return new ResponseEntity<>(chatRoomService.getAllChatRooms(), HttpStatus.OK);
    }

    @PostMapping("/chat/create")
    public ResponseEntity<ChatRoom> createNewChat(@RequestParam UUID firstUserId, @RequestParam UUID secondUserId) {
//        chatRoomService.createNewChat(firstUserId, secondUserId);
        return new ResponseEntity<>(chatRoomService.createNewChat(firstUserId, secondUserId), HttpStatus.CREATED);
    }

    @DeleteMapping("/message/deleteMessage")
    public ResponseEntity<String> deleteMessage(@RequestBody Message message) {
        return new ResponseEntity<>(chatMessageService.deleteMessage(message), HttpStatus.OK);
    }

    @GetMapping("/chat/getLastChatRooms/{userId}")
    public ResponseEntity<List<ChatRoomDTO>> getListLastChatRooms(@PathVariable UUID userId) {
        return new ResponseEntity<>(chatRoomService.getListLastChatRooms(userId), HttpStatus.OK);
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload NewMessageDTO newMessageDTO) {
        chatMessageService.sendNewMessage(newMessageDTO);
    }


    @GetMapping("/chat/getHistory")
    public ResponseEntity<List<Message>> getChatHistory(@RequestParam UUID senderId,
                                                        @RequestParam UUID recipientId) {
        return new ResponseEntity<>(chatRoomService.getHistoryChat(senderId, recipientId), HttpStatus.OK);
    }

    @GetMapping("/chat/getHistory/{chatId}")
    public ResponseEntity<List<Message>> getChatHistory(@PathVariable Long chatId) {
        return new ResponseEntity<>(chatRoomService.getHistoryChat(chatId), HttpStatus.OK);
    }

    @PutMapping("/message/edit")
    public ResponseEntity<Message> editMessage(@RequestBody Message message) {
        return new ResponseEntity<>(chatMessageService.editMessage(message), HttpStatus.OK);
    }


//    @MessageMapping("/chat.sendMessage")
//    @SendTo({"/topic/public", "/user/queue/messages"})
//        public Message sendMessage(Message chatMessage) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null) {
//            logger.info("Authenticated user: {}", authentication.getName());
//        } else {
//            logger.warn("No authenticated user found.");
//        }
//
//        if (simpUserRegistry.getUser(chatMessage.getSenderId()) != null) {
//            logger.info("User with ID {} is present in SimpUserRegistry.", chatMessage.getSenderId());
//        } else {
//            logger.warn("User with ID {} is not present in SimpUserRegistry.", chatMessage.getSenderId());
//        }
//        return chatMessage;
//    }

}