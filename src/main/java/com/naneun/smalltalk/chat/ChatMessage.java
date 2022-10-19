package com.naneun.smalltalk.chat;

import com.naneun.smalltalk.user.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private ChatRoom chatRoom;

    @JoinColumn(updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member sender;

    @NotBlank
    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    private MessageState state;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime sendDateTime;

    /********************************************************************/

    @Builder
    private ChatMessage(ChatRoom chatRoom, String message) {
        this.chatRoom = chatRoom;
        this.message = message;
        this.state = MessageState.UNREAD;
    }

    public static ChatMessage of(ChatRoom chatRoom, String message) {
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .message(message)
                .build();
    }

    /********************************************************************/

    public boolean isUnread() {
        return this.state == MessageState.UNREAD;
    }

    public void changeState(MessageState state) {
        this.state = state;
    }
}
