package com.naneun.smalltalk.chat;

import com.naneun.smalltalk.config.QuerydslConfig;
import com.naneun.smalltalk.container.MySQLTestContainer;
import com.naneun.smalltalk.config.DataJpaConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("junit-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DataJpaConfig.class, QuerydslConfig.class})
class ChatMessageRepositoryTest extends MySQLTestContainer {

    final ChatRoomRepository chatRoomRepository;
    final ChatMessageRepository chatMessageRepository;

    @BeforeAll
    static void setUp() {
        MYSQL_CONTAINER.start();
        assertTrue(MYSQL_CONTAINER.isRunning());
    }

    @Autowired
    ChatMessageRepositoryTest(ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    @Test
    void 아직_읽지_않은_메시지를_조회한다() {

        // given
        ChatRoom testChatRoom = chatRoomRepository.save(ChatRoom.of("Test ChatRoom"));
        List<ChatMessage> newChatMessages = List.of(
                ChatMessage.of(testChatRoom, "UNREAD ChatMessage One"),
                ChatMessage.of(testChatRoom, "UNREAD ChatMessage Two"),
                ChatMessage.of(testChatRoom, "UNREAD ChatMessage Three")
        );
        testChatRoom.addChatMessages(newChatMessages);

        // when
        Integer unreadMessageCount = chatMessageRepository.countByState(MessageState.UNREAD);

        // then
        List<ChatMessage> savedChatMessages = testChatRoom.getChatMessages()
                .stream()
                .filter(ChatMessage::isUnread)
                .collect(Collectors.toList());

        assertThat(unreadMessageCount).isEqualTo(savedChatMessages.size());
        savedChatMessages.forEach((chatMessage) -> assertThat(chatMessage)
                .usingRecursiveComparison()
                .comparingOnlyFields()
                .ignoringFields("sendDateTime")
                .isEqualTo(savedChatMessages.get(newChatMessages.indexOf(chatMessage))));
    }
}
