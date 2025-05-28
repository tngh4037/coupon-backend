package com.example.coupon_backend.domain.member.api;

import com.example.coupon_backend.ControllerTestSupport;
import com.example.coupon_backend.domain.member.api.request.MemberCreateRequest;
import com.example.coupon_backend.domain.member.enums.MemberStatus;
import com.example.coupon_backend.domain.member.service.request.MemberCreateServiceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends ControllerTestSupport {

    @DisplayName("회원가입시 회원을 등록한다.")
    @Test
    public void createMember() throws Exception {
        // given
        MemberCreateRequest requestData = MemberCreateRequest.builder()
                .id("userId")
                .name("kim")
                .build();

        Mockito.when(memberService.save(Mockito.any())).thenReturn(null);

        // when & then
        mockMvc.perform(post("/api/members/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestData)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("회원가입시 아이디는 필수값이다.")
    @Test
    public void createMemberWithoutId() throws Exception {
        // given
        MemberCreateRequest requestData = MemberCreateRequest.builder()
              //  .id("userId")
                .name("kim")
                .build();

        // stubbing
        Mockito.when(memberService.save(Mockito.any(MemberCreateServiceRequest.class))).thenReturn(null);

        // when & then
        mockMvc.perform(post("/api/members/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestData)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("유효하지 않은 요청입니다."))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("C001"))
                .andExpect(jsonPath("$.errors[0].field").value("id"))
                .andExpect(jsonPath("$.errors[0].value").value(""))
                .andExpect(jsonPath("$.errors[0].reason").value("아이디는 필수입니다."))
        ;
    }

    @DisplayName("회원가입시 이름은 필수값이다.")
    @Test
    public void createMemberWithoutName() throws Exception {
        // given
        MemberCreateRequest requestData = MemberCreateRequest.builder()
                .id("userId")
              //  .name("kim")
                .build();

        // stubbing
        Mockito.when(memberService.save(Mockito.any(MemberCreateServiceRequest.class))).thenReturn(null);

        // when & then
        mockMvc.perform(post("/api/members/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestData)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("유효하지 않은 요청입니다."))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("C001"))
                .andExpect(jsonPath("$.errors[0].field").value("name"))
                .andExpect(jsonPath("$.errors[0].value").value(""))
                .andExpect(jsonPath("$.errors[0].reason").value("회원 이름은 필수입니다."))
        ;
    }

    @DisplayName("회원의 상세 정보를 조회한다.")
    @Test
    public void member() throws Exception {
        // given & when & then
        mockMvc.perform(get("/api/members/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("회원 목록을 조회한다.")
    @Test
    public void members() throws Exception {
        // given & when & then
        mockMvc.perform(get("/api/members")
                        .queryParam("name", "kim")
                        .queryParam("status", MemberStatus.NORMAL.name())
                        .queryParam("page", "0")
                        .queryParam("size", "5")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}
