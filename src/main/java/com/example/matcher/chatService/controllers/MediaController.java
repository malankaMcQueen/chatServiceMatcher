package com.example.matcher.chatService.controllers;

import com.example.matcher.chatService.configuration.WebSocketConfiguration;
import com.example.matcher.chatService.model.Message;
import com.example.matcher.chatService.service.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Tag(name = "Media controller", description = "API for operations with media via chat")
@RequestMapping("/ChatService/media")
public class MediaController {

    private MediaService mediaService;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfiguration.class);

    @Operation(summary = "Сохранить фото в истории чата",
            description = "Метод загружает фото в чат")
    @ApiResponse(responseCode = "200", description = "Успешный ответ. Фото успешно загружено")
//    @ApiResponse(responseCode = "409", description = "Чат между данными пользователями уже создан", content = @Content())
    @PostMapping("/photo/upload")
    public ResponseEntity<Message> uploadFile(@RequestParam("chatId") Long chatId, @RequestParam("senderId") UUID senderId,
                                              @RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(mediaService.uploadPhoto(chatId, senderId, file), HttpStatus.OK);
    }

}