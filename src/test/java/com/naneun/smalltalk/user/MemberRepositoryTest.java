package com.naneun.smalltalk.user;

import com.naneun.smalltalk.config.DataJpaConfig;
import com.naneun.smalltalk.config.QuerydslConfig;
import com.naneun.smalltalk.container.MySQLTestContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("junit-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DataJpaConfig.class, QuerydslConfig.class})
class MemberRepositoryTest extends MySQLTestContainer {

    final MemberRepository memberRepository;

    @Autowired
    public MemberRepositoryTest(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @BeforeAll
    static void setUp() {
        MYSQL_CONTAINER.start();
        assertTrue(MYSQL_CONTAINER.isRunning());
    }

    @Test
    void 새로운_회원을_등록한다() {

        // given
        Member newMember = Member.of("new-member");

        // when
        memberRepository.save(newMember);
        Member savedMember = memberRepository.findById(newMember.getId())
                .orElseThrow();

        // then
        assertThat(savedMember)
                .usingRecursiveComparison()
                .ignoringFields(
                        "chatRooms.chatRoom"
                        , "chatRooms.member"
                        , "lastAccessedAt"
                )
                .isEqualTo(newMember);
    }
}