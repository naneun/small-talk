package com.naneun.smalltalk.chat;

import com.naneun.smalltalk.config.QuerydslConfig;
import com.naneun.smalltalk.container.MySQLTestContainer;
import com.naneun.smalltalk.user.Member;
import com.naneun.smalltalk.user.MemberRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("junit-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QuerydslConfig.class)
class ChatRoomRepositoryTest extends MySQLTestContainer {

    final ChatRoomRepository chatRoomRepository;
    final MemberRepository memberRepository;

    @BeforeAll
    static void setUp() {
        MYSQL_CONTAINER.start();
        assertTrue(MYSQL_CONTAINER.isRunning());
    }

    @Autowired
    ChatRoomRepositoryTest(ChatRoomRepository chatRoomRepository, MemberRepository memberRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.memberRepository = memberRepository;
    }

    @Test
    void 새로운_채팅방을_개설한다() {

        // given
        ChatRoom newChatRoom = ChatRoom.of("new-chat-room");
        chatRoomRepository.save(newChatRoom);
        Long roomId = newChatRoom.getId();

        // when
        chatRoomRepository.findById(roomId)
                .orElseThrow();

        ChatRoom savedChatRoom = chatRoomRepository.findAllChatRoomMembersById(roomId)
                .orElseThrow();

        // then
        assertThat(savedChatRoom)
                .usingRecursiveComparison()
                .isEqualTo(newChatRoom);
    }

    @Test
    void 채팅방의_대화내용을_저장한다() {

        // given
        ChatRoom testChatRoom = chatRoomRepository.save(ChatRoom.of("test-chat-room"));
        List<ChatMessage> newChatMessages = List.of(
                ChatMessage.of(testChatRoom, "chat-message-one"),
                ChatMessage.of(testChatRoom, "chat-message-two"),
                ChatMessage.of(testChatRoom, "chat-message-three"),
                ChatMessage.of(testChatRoom, "chat-message-four"),
                ChatMessage.of(testChatRoom, "chat-message-five")
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

    @Test
    void 채팅방에_멤버를_추가한다() {

        // given
        ChatRoom testChatRoom = ChatRoom.of("test-chat-room");
        chatRoomRepository.save(testChatRoom);

        Member newMember = Member.of("new-member");
        memberRepository.save(newMember);

        testChatRoom.pushMember(newMember);

        ChatRoomMember savedChatRoomMember = ChatRoomMember.of(testChatRoom, newMember);

        // when
        ChatRoom updatedChatRoom = chatRoomRepository.findById(testChatRoom.getId())
                .orElseThrow();

        // then
        List<ChatRoomMember> chatRoomMembers = updatedChatRoom.getChatRoomMembers();
        assertThat(chatRoomMembers).contains(savedChatRoomMember);
    }

    @Test
    void 채팅방에_이미_참여한_멤버를_추가하면_무시한다() {

        // given
        ChatRoom testChatRoom = ChatRoom.of("test-chat-room");
        chatRoomRepository.save(testChatRoom);

        Member newMember = Member.of("new-member");
        memberRepository.save(newMember);

        testChatRoom.pushMember(newMember);
        testChatRoom.pushMember(newMember);

        ChatRoomMember savedChatRoomMember = ChatRoomMember.of(testChatRoom, newMember);

        // when
        ChatRoom updatedChatRoom = chatRoomRepository.findById(testChatRoom.getId())
                .orElseThrow();

        // then
        List<ChatRoomMember> chatRoomMembers = updatedChatRoom.getChatRoomMembers();
        assertThat(chatRoomMembers).contains(savedChatRoomMember);
    }

    @Test
    void 채팅방에서_멤버를_제외한다() {

        // given
        ChatRoom testChatRoom = ChatRoom.of("test-chat-room");
        chatRoomRepository.save(testChatRoom);

        Member newMemberOne = Member.of("new-member-one");
        Member newMemberTwo = Member.of("new-member-two");
        memberRepository.saveAll(List.of(newMemberOne, newMemberTwo));

        testChatRoom.pushMember(newMemberOne);
        testChatRoom.pushMember(newMemberTwo);

        ChatRoom updatedChatRoom = chatRoomRepository.findById(testChatRoom.getId())
                .orElseThrow();

        updatedChatRoom.popMember(newMemberTwo);

        ChatRoomMember savedChatRoomMemberOne = ChatRoomMember.of(testChatRoom, newMemberOne);
        ChatRoomMember savedChatRoomMemberTwo = ChatRoomMember.of(testChatRoom, newMemberTwo);

        // when
        chatRoomRepository.findById(updatedChatRoom.getId())
                .orElseThrow();

        // then
        List<ChatRoomMember> chatRoomMembers = testChatRoom.getChatRoomMembers();
        assertAll(
                () -> assertThat(chatRoomMembers).contains(savedChatRoomMemberOne),
                () -> assertThat(chatRoomMembers).doesNotContain(savedChatRoomMemberTwo)
        );
    }

    @Test
    void 키워드가_주어지지_않았을_경우_모든_채팅방_리스트를_조회한다() {

        // given
        ChatRoom testChatRoomOne = ChatRoom.of("test-chat-room-one");
        ChatRoom testChatRoomTwo = ChatRoom.of("test-chat-room-two-target");
        ChatRoom testChatRoomThree = ChatRoom.of("test-chat-room-three-target");
        chatRoomRepository.saveAll(List.of(testChatRoomOne, testChatRoomTwo, testChatRoomThree));

        // when
        List<ChatRoom> foundedChatRooms = chatRoomRepository.findByTitle(null);

        // then
        assertAll(
                () -> assertThat(foundedChatRooms).contains(testChatRoomOne),
                () -> assertThat(foundedChatRooms).contains(testChatRoomTwo),
                () -> assertThat(foundedChatRooms).contains(testChatRoomThree)
        );
    }

    @Test
    void 특정_키워드가_방제목에_포함된_채팅방_리스트를_조회한다() {

        // given
        ChatRoom testChatRoomOne = ChatRoom.of("test-chat-room-one");
        ChatRoom testChatRoomTwo = ChatRoom.of("test-chat-room-two-target");
        ChatRoom testChatRoomThree = ChatRoom.of("test-chat-room-three-target");
        chatRoomRepository.saveAll(List.of(testChatRoomOne, testChatRoomTwo, testChatRoomThree));

        String keyword = "target";

        // when
        List<ChatRoom> foundedChatRooms = chatRoomRepository.findByTitle(keyword);

        // then
        assertAll(
                () -> assertThat(foundedChatRooms).doesNotContain(testChatRoomOne),
                () -> assertThat(foundedChatRooms).contains(testChatRoomTwo),
                () -> assertThat(foundedChatRooms).contains(testChatRoomThree)
        );
    }
}
