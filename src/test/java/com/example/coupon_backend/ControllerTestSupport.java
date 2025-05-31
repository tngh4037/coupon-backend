package com.example.coupon_backend;

import com.example.coupon_backend.domain.member.api.MemberController;
import com.example.coupon_backend.domain.member.service.MemberService;
import com.example.coupon_backend.global.util.MessageHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@Import(MessageHelper.class)
@WebMvcTest(controllers = {
        MemberController.class
})
@AutoConfigureMockMvc(addFilters = false) // except securityFilterChain
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected MemberService memberService;

}
