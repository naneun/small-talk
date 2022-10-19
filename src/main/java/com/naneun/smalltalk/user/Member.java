package com.naneun.smalltalk.user;

import com.naneun.smalltalk.chat.ChatRoomMember;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @JoinColumn
    @OneToMany(fetch = FetchType.LAZY)
    private List<ChatRoomMember> chatRooms;

    @CreatedDate
    private LocalDateTime lastAccessedAt;

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
