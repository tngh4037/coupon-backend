package com.example.coupon_backend.global.config.security.jwt;

import com.example.coupon_backend.IntegrationTestSupport;
import com.example.coupon_backend.domain.member.api.request.MemberLoginRequest;
import com.example.coupon_backend.domain.member.entity.Member;
import com.example.coupon_backend.domain.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@AutoConfigureMockMvc
class JwtAuthenticationFilterTest extends IntegrationTestSupport {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @DisplayName("인증에 성공하면 응답 헤더에 JWT 토큰이 반환된다.")
    @Test
    public void successfulAuthentication_test() throws Exception {
        // given
        joinMember("memberId", "1234");
        MemberLoginRequest loginRequest = new MemberLoginRequest();
        loginRequest.setUsername("memberId");
        loginRequest.setPassword("1234");
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()));
        String jwtToken = resultActions.andReturn().getResponse().getHeader("Authorization");
        assertThat(jwtToken.startsWith(JwtTokenProvider.getTokenPrefix())).isTrue();
    }

    @DisplayName("인증에 실패하면 JWT 토큰이 발급되지 않고 401 예외가 발생한다.")
    @Test
    public void unsuccessfulAuthentication_test() throws Exception {
        // given
        joinMember("memberId", "1234");
        MemberLoginRequest loginRequest = new MemberLoginRequest();
        loginRequest.setUsername("memberId");
        loginRequest.setPassword("failed_pwd");
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()));
        String jwtToken = resultActions.andReturn().getResponse().getHeader("Authorization");
        assertThat(jwtToken).isNull();
    }

    private void joinMember(String memberId, String password) {
        memberRepository.save(Member.join(memberId,
                bCryptPasswordEncoder.encode(password), "test@email.com", "kim"));
    }

}
