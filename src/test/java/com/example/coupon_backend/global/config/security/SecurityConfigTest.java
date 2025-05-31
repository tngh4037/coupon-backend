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

}
