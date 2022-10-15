package com.naneun.smalltalk.chat;

import com.naneun.smalltalk.user.Member;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    // TODO 1 : N -> N : N
    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    private final List<Member> members = new ArrayList<>();

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
        this.members.add(member);
    }

    public void popMember(Member member) {
        this.members.remove(member);
    }

    public void addChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages.addAll(chatMessages);
    }
}
