package com.example.matcher.chatService.kafka;

import com.example.matcher.chatService.aspect.AspectAnnotation;
import com.example.matcher.chatService.service.ChatRoomService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class KafkaConsumerService {


    private final ChatRoomService chatRoomService;
    @AspectAnnotation
    @KafkaListener(topics = "delete_profile", groupId = "${spring.kafka.consumer.group-id}")
    public void listenDeleteProfile(String userId) {
        System.out.println("Received JSON message: " + userId);
        chatRoomService.deletingAllUserChats(UUID.fromString(userId));
    }
}
