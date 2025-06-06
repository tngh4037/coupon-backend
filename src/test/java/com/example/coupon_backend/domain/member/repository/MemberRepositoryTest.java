package com.example.coupon_backend.domain.member.repository;

import com.example.coupon_backend.IntegrationTestSupport;
import com.example.coupon_backend.domain.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class MemberRepositoryTest extends IntegrationTestSupport {

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("회원가입시 회원 정보가 등록된다.")
    @Test
    public void save() throws Exception {
        // given
        Member member = Member.join("userId", "1234", "test@email.com", "kim");

        // when
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).orElse(null);

        // then
        assertThat(savedMember).isEqualTo(findMember);
    }

}