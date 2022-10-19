package com.naneun.smalltalk.user;

import com.naneun.smalltalk.chat.ChatRoomMember;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
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
}
