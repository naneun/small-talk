package com.naneun.smalltalk.chat;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private ChatRoom chatRoom;

    // TODO anonymous -> real name

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
