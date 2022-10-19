package com.naneun.smalltalk.chat;

import com.naneun.smalltalk.user.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomMemberRepository extends CrudRepository<ChatRoomMember, Long> {

    List<ChatRoomMember> findByMember(@Param("member") Member member);
}
