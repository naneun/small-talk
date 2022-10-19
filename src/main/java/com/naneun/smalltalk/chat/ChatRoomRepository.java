package com.naneun.smalltalk.chat;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long> {

    @Query(value = "select cr from ChatRoom cr " +
            "left join fetch cr.chatMessages " +
            "where cr.id = :id")
    Optional<ChatRoom> findById(@Param("id") Long id);

    @Query(value = "select cr from ChatRoom cr " +
            "left join fetch cr.chatRoomMembers crm " +
            "left join fetch crm.member " +
            "where cr.id = :id")
    Optional<ChatRoom> findAllChatRoomMembersById(@Param("id") Long id);
}
