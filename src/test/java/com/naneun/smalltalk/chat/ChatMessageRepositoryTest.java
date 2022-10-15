package com.naneun.smalltalk.chat;

import com.naneun.smalltalk.config.DataJpaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(DataJpaConfig.class)
class ChatMessageRepositoryTest {

    final ChatRoomRepository chatRoomRepository;
    final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatMessageRepositoryTest(ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository) {
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
