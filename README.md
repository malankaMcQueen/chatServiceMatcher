# ChatService

#### Общение через Веб-сокеты происходит по протоколу STOMP
## Описание подключение к сокетам

## 1. Установка соединения
  
- Клиент инициирует WebSocket-соединение, обращаясь к ```/ChatService/ws```.
- При установки соединения в заголовке обязательно должен быть параметр "X-User-Id" в котором записан  UUID пользователя

Пример:
```
stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8083/ChatService/ws/websocket")
// Устанавливаем заголовок X-User-Id
val headers = listOf(StompHeader("X-User-Id", "$userId"))
stompClient.connect(headers)
```

## 2. Подписка на очередь сообщений пользователя

- Клиент должен подписать на тему ```/ChatService/user/queue/messages```
- Когда пользователю отправлено новое сообщение, оно приходит через этот сокет в формате:
```
public class Message {
    Long id;
    UUID senderId;
    ChatRoom chatRoom;
    String content;
    LocalDateTime timestamp;
    MessageStatus status;
}
```

## 3. Подписка на подтверждение отправки сообщения
Это нужно для того чтобы присвоить ID для нового сообщения, и для того чтобы отметить отправлено сообщение или нет

- Необходимо подписать на топик ```/ChatService/topic/user/$userId/message/confirmations```, где userId - UUID пользователя
- Когда пользователь отправляет сообщение, ему от сервера должен прийти ответ через этот топик, в формате:
```
public class Message {
    Long id;
    UUID senderId;
    ChatRoom chatRoom;
    String content;
    LocalDateTime timestamp;
    MessageStatus status;
}
```

## 4. Подписка на отметку сообщения прочитанным

- Необходимо подписаться на топик ```ChatService/topic/user/$userId/messages/read``` , где userId - UUID пользователя
- Когда пользователь, которому было отправлено сообщение прочитает его, через этот топик придет сообщение в формате:
```
  public class Message {
    Long id;
    UUID senderId;
    ChatRoom chatRoom;
    String content;
    LocalDateTime timestamp;
    MessageStatus status;
  }
```

## 5. Подписка на получения информации об удалённом сообщении

- Необходимо подписаться на топик ```ChatService/topic/user/$userId/messages/delete``` , где userId - UUID пользователя
- Когда второй пользователь удаляет сообщение, через этот топик первому пользователю придет сообщение в формате:
```
  public class DeleteMessageDTO {
    private Long messageId;
    private UUID senderId;
    private Long chatRoomId;
}
```

## 6. Подписка на получения информации об отредактированном сообщении

- Необходимо подписаться на топик ```ChatService/topic/user/$userId/messages/edit``` , где userId - UUID пользователя
- Когда второй пользователь редактирует сообщение, через этот топик первому пользователю придет сообщение в формате:
```
public class EditMessageDTO {
    private Long messageId;
    private Long chatRoomId;
    private UUID senderId;
    private String newContent;
}
```

## 7. Отправка сообщения
- Отправка сообщения происходит на адрес ```/ChatService/chat/message/send```
- Отправляется в формате:
```
public class NewMessageDTO {
    UUID senderId;
    Long chatRoomId;
    String content;
}
```

Где senderId - UUID отправителя.

## 8. Пометить сообщение прочитанным
- Для того чтобы пометить сообщение прочитанным нужно отправить сообщение на адрес ```/ChatService/chat/message/read```
- Формат сообщения:
```
public class ReadMessageDTO {
    Long id;
    Long chatRoomId;
    UUID senderId;
    String content;
    MessageStatus status;
}
```

## 9. Удалить сообщение
- Для того чтобы удалить сообщение, нужно отправить сообщение на адрес ```/ChatService/chat/message/read```
- Формат сообщения:
```
public class DeleteMessageDTO {
    Long messageId;
    UUID senderId;
    Long chatRoomId;
}
```

## 10. Редактирование сообщения
- Для того чтобы редактировать сообщение, нужно отправить сообщение на адрес ```/ChatService/chat/message/read```
- Формат сообщения:
```
public class EditMessageDTO {
    Long messageId;
    Long chatRoomId;
    UUID senderId;
    String newContent;
}
```

### Все сообщения отправляются в формате Json
- Без названия класса
- Без указания типа переменной

Пример сообщения для редактирования:
```
{
    messageId: 131,
    chatRoomId: 11,
    senderId: 1620a8af-5cae-4973-8576-a43beda7eb9e,
    newContent: "Hello this is new content"
}
```