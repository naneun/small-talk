package com.naneun.smalltalk.chat;

import com.naneun.smalltalk.user.Member;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private final List<ChatRoomMember> chatRoomMembers = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private final List<ChatMessage> chatMessages = new ArrayList<>();

    /********************************************************************/

    @Builder
    private ChatRoom(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static ChatRoom of(String title) {
        return ChatRoom.builder()
                .title(title)
                .build();
    }

    /********************************************************************/

    public void pushMember(Member member) {
        ChatRoomMember chatRoomMember = ChatRoomMember.of(this, member);
        if (this.chatRoomMembers.contains(chatRoomMember)) {
            return;
        }
        member.enterChatRoom(chatRoomMember);
        this.chatRoomMembers.add(chatRoomMember);
    }

    public void popMember(Member member) {
        ChatRoomMember chatRoomMember = ChatRoomMember.of(this, member);
        member.leaveChatRoom(chatRoomMember);
        this.chatRoomMembers.remove(chatRoomMember);
    }

    public void addChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages.addAll(chatMessages);
    }

    /********************************************************************/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoom chatRoom = (ChatRoom) o;
        return Objects.equals(id, chatRoom.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
