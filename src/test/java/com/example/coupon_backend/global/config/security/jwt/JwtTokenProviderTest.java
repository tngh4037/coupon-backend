package com.example.coupon_backend.global.config.security.jwt;

import com.example.coupon_backend.domain.member.entity.Member;
import com.example.coupon_backend.domain.member.enums.MemberRole;
import com.example.coupon_backend.global.config.security.service.CustomUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {
    
    @DisplayName("회원의 식별자와 권한을 기반으로 JWT 토큰을 생성할 수 있다.")
    @Test
    public void createJwtTokenTest() throws Exception {
        // given
        Member member = Member.builder().id(1L).role(MemberRole.GOLD).build();
        CustomUserDetails userDetails = new CustomUserDetails(member);

        // when
        String jwtToken = JwtTokenProvider.create(userDetails);

        // then
        assertThat(jwtToken.startsWith(JwtTokenProvider.getTokenPrefix())).isTrue();
    }

    @DisplayName("토큰을 디코딩하면 회원 정보가 조회된다.")
    @Test
    public void verifyTest() throws Exception {
        // given
        Member member = Member.builder().id(1L).role(MemberRole.GOLD).build();
        CustomUserDetails userDetails = new CustomUserDetails(member);
        String jwtToken = JwtTokenProvider.create(userDetails).replace(JwtTokenProvider.getTokenPrefix(), "");

        // when
        CustomUserDetails customUserDetails = JwtTokenProvider.verify(jwtToken);

        // then
        assertThat(member.getId()).isEqualTo(customUserDetails.getMember().getId());
        assertThat(member.getRole()).isEqualTo(customUserDetails.getMember().getRole());
    }

}