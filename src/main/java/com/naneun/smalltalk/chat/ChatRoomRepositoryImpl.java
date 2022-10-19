package com.naneun.smalltalk.chat;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.naneun.smalltalk.chat.QChatRoom.chatRoom;

@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<ChatRoom> findByTitle(String keyword) {
        return queryFactory.selectFrom(chatRoom)
                .where(containsKeyword(keyword))
                .fetch();
    }

    private BooleanExpression containsKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }

        return chatRoom.title.contains(keyword);
    }
}
