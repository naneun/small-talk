package com.naneun.smalltalk.chat;

import org.springframework.data.repository.CrudRepository;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {

    Integer countByState(MessageState state);
}
