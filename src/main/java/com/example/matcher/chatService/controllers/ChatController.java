package com.example.matcher.chatService.controllers;

import com.example.matcher.chatService.aspect.AspectAnnotation;
import com.example.matcher.chatService.configuration.WebSocketConfiguration;
import com.example.matcher.chatService.dto.ChatRoomWithLastMessageDTO;
import com.example.matcher.chatService.dto.message.DeleteMessageDTO;
import com.example.matcher.chatService.dto.message.EditMessageDTO;
import com.example.matcher.chatService.model.ChatRoom;
import com.example.matcher.chatService.model.Message;
import com.example.matcher.chatService.service.ChatMessageService;
import com.example.matcher.chatService.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
@Tag(name = "Chat controller", description = "API for operations with chat and message")
@RequestMapping("/ChatService/")
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

    @Operation(summary = "Создать новый чат",
            description = "Метод создаёт новый чат между двумя пользователями")
    @ApiResponse(responseCode = "201", description = "Успешный ответ. Чат успешно создан. Возвращает модель чата")
    @ApiResponse(responseCode = "409", description = "Чат между данными пользователями уже создан", content = @Content())
    @PostMapping("/chat/create")
    public ResponseEntity<ChatRoom> createNewChat(@RequestParam UUID firstUserId, @RequestParam UUID secondUserId) {
//        chatRoomService.createNewChat(firstUserId, secondUserId);
        return new ResponseEntity<>(chatRoomService.createNewChat(firstUserId, secondUserId), HttpStatus.CREATED);
    }

    @Operation(summary = "Удаление сообщения",
            description = "Метод удаляет сообщение из истории")
    @ApiResponse(responseCode = "200", description = "Успешный ответ. Сообщение удалено или не найдено(ничего на сервере не изменилось). Возвращает строку \"succes\"")
    @DeleteMapping("/message/deleteMessage")
    @AspectAnnotation
    public ResponseEntity<String> deleteMessage(@RequestBody DeleteMessageDTO message) {
        return new ResponseEntity<>(chatMessageService.deleteMessage(message), HttpStatus.OK);
    }
    @Operation(summary = "Список последних диалогов пользователя",
            description = "Метод возвращает список последних диалогов для конкретного пользователя")
    @ApiResponse(responseCode = "200", description = "Успешный ответ. Возвращает список моделей ChatRoomWithLastMessageDTO")
    @GetMapping("/chat/getLastChatRooms/{userId}")
    public ResponseEntity<List<ChatRoomWithLastMessageDTO>> getListLastChatRooms(@PathVariable UUID userId) {
        return new ResponseEntity<>(chatRoomService.getListLastChatRooms(userId), HttpStatus.OK);
    }
    @Operation(summary = "Получить историю всего чата ПО ID ПОЛЬЗОВАТЕЛЕЙ",
            description = "Возвращает список всех сообщений в данном чате, отсортированных по времени (List[0] - последнее сообщение")
    @ApiResponse(responseCode = "200", description = "Успешный ответ. Возвращает список моделей Message")
    @GetMapping("/chat/getHistory")
    public ResponseEntity<List<Message>> getChatHistory(@RequestParam UUID senderId,
                                                        @RequestParam UUID recipientId) {
        return new ResponseEntity<>(chatRoomService.getHistoryChat(senderId, recipientId), HttpStatus.OK);
    }
    @Operation(summary = "Получить историю всего чата ПО ID чата",
            description = "Возвращает список всех сообщений в данном чате, отсортированных по времени (List[0] - последнее сообщение")
    @ApiResponse(responseCode = "200", description = "Успешный ответ. Возвращает список моделей Message")
    @GetMapping("/chat/getHistory/{chatId}")
    public ResponseEntity<List<Message>> getChatHistory(@PathVariable Long chatId) {
        return new ResponseEntity<>(chatRoomService.getHistoryChat(chatId), HttpStatus.OK);
    }
    @Operation(summary = "Редактирование сообщения",
            description = "Редактирование отправленного сообщения")
    @ApiResponse(responseCode = "200", description = "Успешный ответ. Возвращает отредактированные сообщение, модель Message")
    @ApiResponse(responseCode = "404", description = "Сообщение с данным ID не найдено.", content = @Content())
    @PutMapping("/message/edit")
    public ResponseEntity<Message> editMessage(@RequestBody EditMessageDTO editMessageDTO) {
        return new ResponseEntity<>(chatMessageService.editMessage(editMessageDTO), HttpStatus.OK);
    }

}