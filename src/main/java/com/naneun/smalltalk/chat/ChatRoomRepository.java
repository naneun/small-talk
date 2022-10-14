package com.naneun.smalltalk.chat;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long> {

    @EntityGraph(attributePaths = {"chatMessages"})
    Optional<ChatRoom> findById(@Param("id") Long id);
}
