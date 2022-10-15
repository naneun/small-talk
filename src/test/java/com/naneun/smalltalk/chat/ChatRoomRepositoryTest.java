package com.naneun.smalltalk.chat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ChatRoomRepositoryTest {

    final ChatRoomRepository chatRoomRepository;

    @Autowired
    ChatRoomRepositoryTest(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Test
    void 새로운_채팅방을_생성한다() {

        // given
        ChatRoom newChatRoom = ChatRoom.of("New ChatRoom");
        chatRoomRepository.save(newChatRoom);
        Long roomId = newChatRoom.getId();

        // when
        chatRoomRepository.findById(roomId)
                .orElseThrow();

        ChatRoom savedChatRoom = chatRoomRepository.findAllMembersById(roomId)
                .orElseThrow();

        // then
        assertThat(savedChatRoom).usingRecursiveComparison().isEqualTo(newChatRoom);
    }

    @Test
    void 대화내용을_저장한다() {

        // given
        ChatRoom testChatRoom = chatRoomRepository.save(ChatRoom.of("Test ChatRoom"));
        List<ChatMessage> newChatMessages = List.of(
                ChatMessage.of(testChatRoom, "ChatMessage One"),
                ChatMessage.of(testChatRoom, "ChatMessage Two"),
                ChatMessage.of(testChatRoom, "ChatMessage Three"),
                ChatMessage.of(testChatRoom, "ChatMessage Four"),
                ChatMessage.of(testChatRoom, "ChatMessage Five")
        );
        testChatRoom.addChatMessages(newChatMessages);

        // when
        ChatRoom savedChatRoom = chatRoomRepository.findById(testChatRoom.getId())
                .orElseThrow();

        // then
        List<ChatMessage> savedChatMessages = savedChatRoom.getChatMessages();
        savedChatMessages.forEach((chatMessage) -> assertThat(chatMessage)
                .usingRecursiveComparison()
                .isEqualTo(savedChatMessages.get(newChatMessages.indexOf(chatMessage))));
    }
}
