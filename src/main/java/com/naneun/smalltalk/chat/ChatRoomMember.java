package com.naneun.smalltalk.chat;

import com.naneun.smalltalk.user.Member;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"chatRoom", "member"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(updatable = false, name = "chatRoom")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private ChatRoom chatRoom;

    @JoinColumn(updatable = false, name = "member")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Member member;

    /********************************************************************/

    @Builder
    private ChatRoomMember(Long id, ChatRoom chatRoom, Member member) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.member = member;
    }

    public static ChatRoomMember of(ChatRoom chatRoom, Member member) {
        return ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();
    }

    /********************************************************************/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoomMember that = (ChatRoomMember) o;
        return Objects.equals(chatRoom, that.chatRoom) && Objects.equals(member, that.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatRoom, member);
    }
}
