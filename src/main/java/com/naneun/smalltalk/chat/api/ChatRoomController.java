package com.naneun.smalltalk.chat.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat-rooms")
@Slf4j
public class ChatRoomController {

    @PostMapping
    public ChatRoomResponse createChatRoom(@RequestBody ChatRoomRequest chatRoomRequest) {
        log.info("{}", chatRoomRequest);
        return ChatRoomResponse.of(chatRoomRequest.toEntity());
    }
}
