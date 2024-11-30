package com.example.matcher.chatService.repository;



import com.example.matcher.chatService.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<Message, Long> {

//    List<Message> findByChatId(String chatId);

//    long countBySenderIdAndRecipientIdAndStatus(String senderId, String recipientId, MessageStatus status);
//
//    List<Message> findByChatId(String chatId);
//
//    @Modifying
//    @Query("UPDATE Message cm SET cm.status = :status WHERE cm.senderId = :senderId AND cm.recipientId = :recipientId")
//    void updateMessageStatus(String senderId, String recipientId, MessageStatus status);
}