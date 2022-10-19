package com.naneun.smalltalk.user;

import com.naneun.smalltalk.chat.ChatRoomMember;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @JoinColumn
    @OneToMany(fetch = FetchType.LAZY)
    private final List<ChatRoomMember> chatRooms = new ArrayList<>();

    /********************************************************************/

    @Builder
    private Member(String name) {
        this.name = name;
    }

    public static Member of(String name) {
        return Member.builder()
                .name(name)
                .build();
    }

    /********************************************************************/

    public void enterChatRoom(ChatRoomMember chatRoomMember) {
        chatRooms.add(chatRoomMember);
    }

    public void leaveChatRoom(ChatRoomMember chatRoomMember) {
        chatRooms.remove(chatRoomMember);
    }

    /********************************************************************/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
