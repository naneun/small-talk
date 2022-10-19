package com.naneun.smalltalk.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long> {

    @Query(value = "select m from Member m " +
            "left join fetch m.chatRoomMembers crms " +
            "left join fetch crms.chatRoom " +
            "where m.id = :id")
    Optional<Member> findById(@Param("id") Long id);
}
