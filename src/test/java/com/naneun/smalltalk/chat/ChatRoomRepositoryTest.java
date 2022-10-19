package com.naneun.smalltalk.chat;

import com.naneun.smalltalk.config.DataJpaConfig;
import com.naneun.smalltalk.config.QuerydslConfig;
import com.naneun.smalltalk.container.MySQLTestContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("junit-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QuerydslConfig.class)
class ChatRoomRepositoryTest extends MySQLTestContainer {

    final ChatRoomRepository chatRoomRepository;

    @BeforeAll
    static void setUp() {
        MYSQL_CONTAINER.start();
        assertTrue(MYSQL_CONTAINER.isRunning());
    }

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

        ChatRoom savedChatRoom = chatRoomRepository.findAllChatRoomMembersById(roomId)
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
