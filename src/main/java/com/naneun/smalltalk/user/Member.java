package com.naneun.smalltalk.user;

import com.naneun.smalltalk.chat.ChatRoom;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Getter
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    // TODO Last Access Location (longitude, latitude)
    // TODO Last Accessed Time

    // TODO N : 1 -> N : N
    @JoinColumn
    @ManyToOne
    private ChatRoom chatRoom;

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
