package com.naneun.smalltalk.chat;

import com.naneun.smalltalk.user.Member;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
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
        this.chatRoomMembers.add(chatRoomMember);
    }

    public void popMember(Member member) {
        ChatRoomMember chatRoomMember = ChatRoomMember.of(this, member);
        this.chatRoomMembers.remove(chatRoomMember);
    }

    public void addChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages.addAll(chatMessages);
    }
}
