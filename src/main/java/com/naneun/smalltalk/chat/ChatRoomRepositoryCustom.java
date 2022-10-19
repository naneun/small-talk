package com.naneun.smalltalk.chat;

import java.util.List;

public interface ChatRoomRepositoryCustom {

    List<ChatRoom> findByTitle(String keyword);
}
