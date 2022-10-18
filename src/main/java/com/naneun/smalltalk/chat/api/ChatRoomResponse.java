package com.naneun.smalltalk.chat.api;

import com.naneun.smalltalk.chat.ChatRoom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatRoomResponse {

    private final Long id;
    private final String title;

    public static ChatRoomResponse of(ChatRoom chatRoom) {
        return new ChatRoomResponse(chatRoom.getId(), chatRoom.getTitle());
    }
}
