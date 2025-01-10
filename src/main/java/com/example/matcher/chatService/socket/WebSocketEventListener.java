package com.example.matcher.chatService.socket;

import com.example.matcher.chatService.configuration.WebSocketConfiguration;
import com.example.matcher.chatService.dto.StatusConnectionUpdate;
import com.example.matcher.chatService.kafka.KafkaProducerService;
import com.example.matcher.chatService.model.StatusConnection;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.UUID;

@Component
@AllArgsConstructor
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    private final KafkaProducerService kafkaProducerService;
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        String userId = headerAccessor.getUser() != null ? headerAccessor.getUser().getName() : "Unknown";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("USER {} connected", authentication.getName());
        StatusConnectionUpdate statusConnectionUpdate = new StatusConnectionUpdate(UUID.fromString(authentication.getName()), StatusConnection.ONLINE);
        kafkaProducerService.sendMessage(statusConnectionUpdate, "status-connection-update");
        // Логика при подключении пользователя
//        System.out.println("User connected: " + userId);

        // Например, обновляем статус пользователя в Redis
        // redisTemplate.opsForValue().set("user_status:" + userId, "online");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        String userId = headerAccessor.getUser() != null ? headerAccessor.getUser().getName() : "Unknown";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        logger.info("USER {} DISCONNECTED", authentication.getName());
        StatusConnectionUpdate statusConnectionUpdate = new StatusConnectionUpdate(UUID.fromString(authentication.getName()), StatusConnection.OFFLINE);
        kafkaProducerService.sendMessage(statusConnectionUpdate, "status_connection_update");
        // Логика при отключении пользователя
//        System.out.println("User disconnected: " + userId);

        // Например, удаляем статус из Redis
        // redisTemplate.delete("user_status:" + userId);
    }
}
