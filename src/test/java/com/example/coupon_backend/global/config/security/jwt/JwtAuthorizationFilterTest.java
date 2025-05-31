package com.example.coupon_backend.global.config.security.jwt;

import com.example.coupon_backend.IntegrationTestSupport;
import com.example.coupon_backend.domain.member.entity.Member;
import com.example.coupon_backend.domain.member.enums.MemberRole;
import com.example.coupon_backend.global.config.security.service.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
public class JwtAuthorizationFilterTest extends IntegrationTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void doFilterInternalTest_success() throws Exception {
        // given
        Member member = Member.builder().id(1L).role(MemberRole.GOLD).build();
        CustomUserDetails userDetails = new CustomUserDetails(member);
        String token = JwtTokenProvider.create(userDetails);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/bills/hello")
                        .header("Authorization", token));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void doFilterInternalTest_authentication_fail() throws Exception {
        // given
        Member member = Member.builder().id(1L).role(MemberRole.GOLD).build();
        CustomUserDetails userDetails = new CustomUserDetails(member);
        String token = JwtTokenProvider.create(userDetails);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/bills/hello")
                      //  .header("Authorization", token)
        );

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void doFilterInternalTest_authorization_fail() throws Exception {
        // given
        Member member = Member.builder().id(1L).role(MemberRole.GOLD).build();
        CustomUserDetails userDetails = new CustomUserDetails(member);
        String token = JwtTokenProvider.create(userDetails);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/order/hello")
                        .header("Authorization", token)
        );

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void doFilterInternalTest_not_found_fail() throws Exception {
        // given
        Member member = Member.builder().id(1L).role(MemberRole.VIP).build();
        CustomUserDetails userDetails = new CustomUserDetails(member);
        String token = JwtTokenProvider.create(userDetails);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/order/hello")
                        .header("Authorization", token)
        );

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
