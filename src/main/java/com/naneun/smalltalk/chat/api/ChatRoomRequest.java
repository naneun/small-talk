package com.naneun.smalltalk.chat.api;

import com.naneun.smalltalk.chat.ChatRoom;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatRoomRequest {

    private String title;

    public ChatRoom toEntity() {
        return ChatRoom.builder()
                .id(1L)
                .title(title)
                .build();
    }
}
