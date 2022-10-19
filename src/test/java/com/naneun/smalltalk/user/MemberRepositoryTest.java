package com.naneun.smalltalk.user;

import com.naneun.smalltalk.chat.ChatRoom;
import com.naneun.smalltalk.chat.ChatRoomMember;
import com.naneun.smalltalk.chat.ChatRoomMemberRepository;
import com.naneun.smalltalk.chat.ChatRoomRepository;
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
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("junit-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DataJpaConfig.class, QuerydslConfig.class})
class MemberRepositoryTest extends MySQLTestContainer {

    final MemberRepository memberRepository;
    final ChatRoomRepository chatRoomRepository;
    final ChatRoomMemberRepository chatRoomMemberRepository;

    @Autowired
    public MemberRepositoryTest(MemberRepository memberRepository, ChatRoomRepository chatRoomRepository
            , ChatRoomMemberRepository chatRoomMemberRepository) {

        this.memberRepository = memberRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomMemberRepository = chatRoomMemberRepository;
    }

    @BeforeAll
    static void setUp() {
        MYSQL_CONTAINER.start();
        assertTrue(MYSQL_CONTAINER.isRunning());
    }

    @Test
    void 새로운_회원을_등록한다() {

        // given
        Member newMember = Member.of("new-member");

        // when
        memberRepository.save(newMember);
        Member savedMember = memberRepository.findById(newMember.getId())
                .orElseThrow();

        // then
        assertThat(savedMember)
                .usingRecursiveComparison()
                .ignoringFields("chatRooms.chatRoom", "chatRooms.member")
                .isEqualTo(newMember);
    }

    @Test
    void 회원이_현재_참여_중인_채팅방_리스트를_조회한다() {

        // given
        Member targetMember = Member.of("target-member");
        memberRepository.save(targetMember);

        ChatRoom targetChatRoomOne = ChatRoom.of("target-chat-room-one");
        ChatRoom targetChatRoomTwo = ChatRoom.of("target-chat-room-two");
        ChatRoom targetChatRoomThree = ChatRoom.of("target-chat-room-three");
        chatRoomRepository.saveAll(List.of(targetChatRoomOne, targetChatRoomTwo, targetChatRoomThree));

        targetChatRoomOne.pushMember(targetMember);
        targetChatRoomTwo.pushMember(targetMember);

        List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findByMember(targetMember);
        for (ChatRoomMember chatRoomMember : chatRoomMembers) {
            targetMember.enterChatRoom(chatRoomMember);
        }

        // when
        memberRepository.findById(targetMember.getId())
                .orElseThrow();

        // then
        List<ChatRoomMember> savedChatRoomMembers = targetMember.getChatRoomMembers();
        assertAll(
                () -> assertThat(savedChatRoomMembers).hasSize(chatRoomMembers.size()),
                () -> assertThat(savedChatRoomMembers).containsAll(chatRoomMembers)
        );
    }
}
