package com.naneun.smalltalk.chat;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long> {

    // TODO Exclude deleted messages
    @Query(value = "select cr from ChatRoom cr " +
            "left join fetch cr.chatMessages " +
            "where cr.id = :id")
    Optional<ChatRoom> findById(@Param("id") Long id);

    @Query(value = "select cr from ChatRoom cr " +
            "left join fetch cr.members " +
            "where cr.id = :id")
    Optional<ChatRoom> findAllMembersById(@Param("id") Long id);
}
