package com.example.matcher.chatService.repository;

import com.example.matcher.chatService.model.ChatRoom;
import com.example.matcher.chatService.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT c FROM ChatRoom c WHERE (c.firstUserId = :firstUserId AND c.secondUserId = :secondUserId) " +
            "OR (c.firstUserId = :secondUserId AND c.secondUserId = :firstUserId)")
    Optional<ChatRoom> findChatRoomBetweenUsers(@Param("firstUserId") UUID firstUserId,
                                                @Param("secondUserId") UUID secondUserId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ChatRoom c WHERE c.firstUserId = :userId OR c.secondUserId = :userId")
    void deleteAllChatRoomsWithUserId(UUID userId);

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.firstUserId = :userId OR cr.secondUserId = :userId ORDER BY cr.timeLastUpdate DESC")
    List<ChatRoom> findListLastChatRooms(@Param("userId") UUID userId);

//    @Query("SELECT cr FROM ChatRoom cr " +
//            "LEFT JOIN FETCH cr.messageList m " +
//            "WHERE cr.firstUserId = :userId OR cr.secondUserId = :userId " +
//            "AND m.timestamp = (SELECT MAX(msg.timestamp) FROM Message msg WHERE msg.chatRoom.id = cr.id) " +
//            "ORDER BY cr.timeLastUpdate DESC")
//    List<ChatRoom> findListLastChatRoomsWithLastMessage(@Param("userId") UUID userId);

    @Query("SELECT cr, m FROM ChatRoom cr " +
            "LEFT JOIN Message m ON m.chatRoom = cr AND m.time = " +
            "(SELECT MAX(msg.time) FROM Message msg WHERE msg.chatRoom = cr) " +
            "WHERE (cr.firstUserId = :userId OR cr.secondUserId = :userId) " +
            "ORDER BY cr.timeLastUpdate DESC")
    List<Object[]> findListLastChatRoomsWithLastMessage(@Param("userId") UUID userId);



    @Query("SELECT m FROM Message m WHERE (m.chatRoom.firstUserId = :senderId AND m.chatRoom.secondUserId = :recipientId) " +
            "OR (m.chatRoom.firstUserId = :recipientId AND m.chatRoom.secondUserId = :senderId) " +
            "ORDER BY m.time DESC")
    List<Message> findChatHistoryBetweenUsers(@Param("senderId") UUID senderId,
                                              @Param("recipientId") UUID recipientId);

    @Query("SELECT m FROM Message m WHERE m.chatRoom.id = :chatId ORDER BY m.time DESC")
    List<Message> findChatHistoryByChatId(@Param("chatId") Long chatId);


}

