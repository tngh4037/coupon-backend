package com.example.coupon_backend.docs.member;

import com.example.coupon_backend.docs.RestDocsSupport;
import com.example.coupon_backend.domain.member.api.MemberController;
import com.example.coupon_backend.domain.member.api.request.MemberCreateRequest;
import com.example.coupon_backend.domain.member.service.MemberService;
import com.example.coupon_backend.domain.member.service.request.MemberCreateServiceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class MemberControllerDocsTest extends RestDocsSupport {

    private final MemberService memberService = Mockito.mock(MemberService.class);

    @DisplayName("회원 가입 API")
    @Test
    public void join() throws Exception {
        // given
        MemberCreateRequest requestData = MemberCreateRequest.builder()
                .id("testId")
                .name("testName")
                .build();

        // stubbing
        Mockito.when(memberService.save(Mockito.any(MemberCreateServiceRequest.class)))
                .thenReturn(1L);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/members/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(1L))
                // API 문서를 위한 체이닝
                .andDo(MockMvcRestDocumentation.document("member-new", // 참고) "member-new" : 해당 테스트에 대한 임의의 식별자
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), // 결과로 나오는 내용에서 요청 json 을 ( 한줄이 아닌 ) 보기편한 형태로 보여줌
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()), // 결과로 나오는 내용에서 응답 json 을 ( 한줄이 아닌 ) 보기편한 형태로 보여줌
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("id").type(JsonFieldType.STRING).description("아이디"),
                                PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("이름")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                                PayloadDocumentation.fieldWithPath("data").type(JsonFieldType.NUMBER).description("회원번호")
                        )
                ));
    }

    @Override
    protected Object initController() {
        // MemberController 가 필요한 의존성은 Mocking 해서 주입
        return new MemberController(memberService);
    }
}
