package com.example.matcher.chatService.socket;

import com.example.matcher.chatService.dto.StatusConnectionUpdate;
import com.example.matcher.chatService.exception.InvalidCredentialsException;
import com.example.matcher.chatService.kafka.KafkaProducerService;
import com.example.matcher.chatService.model.StatusConnection;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.UUID;

@Component
@AllArgsConstructor
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    private final KafkaProducerService kafkaProducerService;
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//        String userId = accessor.getFirstNativeHeader("X-User-Id"); // Получаем ID пользователя из заголовков
        Principal user = StompHeaderAccessor.wrap(event.getMessage()).getUser();
        if (user != null) {
            logger.info("User connected: {}", user.getName());
        }
        else {
            throw new InvalidCredentialsException("Not found userID in Header: X-User-Id: {uuid}");
        }
        String userId = user.getName();
        if (userId != null) {
            StatusConnectionUpdate statusConnectionUpdate = new StatusConnectionUpdate(UUID.fromString(userId), StatusConnection.ONLINE);
            kafkaProducerService.sendMessage(statusConnectionUpdate, "status_connection_update");
            logger.info("User connected: {}", userId);
        } else {
            logger.warn("Connection attempt without user ID.");
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        Principal user = StompHeaderAccessor.wrap(event.getMessage()).getUser();
        if (user != null) {
            logger.info("User connected: {}", user.getName());
        }
        else {
            throw new InvalidCredentialsException("Not found userID in Header: X-User-Id: {uuid}");
        }
        String userId = user.getName();

        if (userId != null) {
            StatusConnectionUpdate statusConnectionUpdate = new StatusConnectionUpdate(UUID.fromString(userId), StatusConnection.OFFLINE);
            kafkaProducerService.sendMessage(statusConnectionUpdate, "status_connection_update");
            logger.info("User disconnect: {}", userId);
        } else {
            logger.warn("User disconnect without user ID.");
        }
    }
}
