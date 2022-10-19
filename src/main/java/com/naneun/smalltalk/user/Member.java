package com.naneun.smalltalk.user;

import com.naneun.smalltalk.chat.ChatRoomMember;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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
    private final List<ChatRoomMember> chatRoomMembers = new ArrayList<>();

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
        chatRoomMembers.add(chatRoomMember);
    }

    public void leaveChatRoom(ChatRoomMember chatRoomMember) {
        chatRoomMembers.remove(chatRoomMember);
    }
}
