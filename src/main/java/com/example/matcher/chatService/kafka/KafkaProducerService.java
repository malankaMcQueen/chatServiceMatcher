package com.example.matcher.chatService.kafka;

import com.example.matcher.chatService.dto.StatusConnectionUpdate;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaProducerService {


    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

//    public void sendMessage(ProfileEvent profileEvent, String topicName) {
//        logger.info("[KAFKA] SEND MESSAGE: " + profileEvent);
//        profileEventKafkaTemplate.send(topicName, profileEvent);
//    }

    public void sendMessage(StatusConnectionUpdate statusConnectionUpdate, String topicName) {
        logger.info("[KAFKA] SEND MESSAGE: " + statusConnectionUpdate);
        kafkaTemplate.send(topicName, statusConnectionUpdate);
    }
//    public void sendMessage(ProfileUpdateForKafka profileEvent, String topicName) {
//        logger.info("[KAFKA] SEND MESSAGE: " + profileEvent);
//        profileEventKafkaTemplate.send(topicName, profileEvent);
//    }
//
//    public void sendMessage(String userId, String topicName) {
//        stringKafkaTemplate.send(topicName, userId);
//    }
}


