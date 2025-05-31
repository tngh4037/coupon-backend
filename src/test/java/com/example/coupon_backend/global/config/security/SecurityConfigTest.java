package com.example.coupon_backend.global.config.security;

import com.example.coupon_backend.IntegrationTestSupport;
import com.example.coupon_backend.global.error.code.CommonErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
class SecurityConfigTest extends IntegrationTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("인증되지 않은 유저가 인증이 필요한 경로에 접근시 401 오류가 발생한다.")
    @Test
    public void authentication_test() throws Exception {

        ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/bills/test").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonErrorCode.UNAUTHORIZED_ERROR.getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonErrorCode.UNAUTHORIZED_ERROR.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(CommonErrorCode.UNAUTHORIZED_ERROR.getHttpStatus().value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isEmpty());

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int httpStatusCode = resultActions.andReturn().getResponse().getStatus();
        System.out.println("responseBody = " + responseBody);
        System.out.println("httpStatusCode = " + httpStatusCode);

        assertThat(httpStatusCode).isEqualTo(401);
    }

    @DisplayName("인증되지 않은 사용자가 권한이 필요한 경로에 접근시 401 오류가 발생한다.")
    @Test
    public void authorization_test() throws Exception {

        ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/order/test").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonErrorCode.UNAUTHORIZED_ERROR.getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonErrorCode.UNAUTHORIZED_ERROR.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(CommonErrorCode.UNAUTHORIZED_ERROR.getHttpStatus().value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isEmpty());

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int httpStatusCode = resultActions.andReturn().getResponse().getStatus();
        System.out.println("responseBody = " + responseBody);
        System.out.println("httpStatusCode = " + httpStatusCode);

        assertThat(httpStatusCode).isEqualTo(401);
    }

}

// 참고) MockMvc 테스트시 시큐리티 검증 참고
// - WAS 없음 ( MockMvc 는 WAS 없이 FilterChain을 직접 구성해서 실행함. )
// - (MockMvc 테스트의 경우) 시큐리티 오류가 발생해서 sendError(...) 등의 호출로 WAS까지 오류가 전파되더라도 DispatcherType.ERROR 상황이 트리거되지 않음 -> BasicErrorController가 호출되지 않음
//   ㄴ 그래서 응답은 403으로 받아오는데, responseBody가 비어있음
//   ㄴ 이 경우, 실제 프로덕션 환경과 일관되게 테스트하려면, exceptionHandling 을 등록하자.
