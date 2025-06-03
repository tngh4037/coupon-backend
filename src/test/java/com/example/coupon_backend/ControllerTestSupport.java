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
@AutoConfigureMockMvc(addFilters = false) // except securityFilterChain ( 마치 시큐리티 없는 상태처럼 동작, 스프링 시큐리티 필터 자체도 안 적용됨, 로그인, 인증, 인가 관련 아무 제한 없이 접근 가능 )
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected MemberService memberService;

}

// [ 고민: 컨트롤러 테스트 시, 시큐리티와의 연동은 어떻게 할 것인가 ? ]
// 참고) @SpringBootTest / @WebMvcTest 에서의 스프링 시큐리티 동작 참고
// - @SpringBootTest
//  ㄴ 통합테스트는 전체 애플리케이션 컨텍스트를 로드한다.
//  ㄴ 따라서, 스프링 시큐리티 설정이 모두 적용된다. ( 내가 개별 설정한 것도 )
//  ㄴ 실제 서버처럼 돌아가기 때문에 보안 설정도 포함한 통합 테스트에 적합하다.
//
// - @WebMvcTest
//  ㄴ Web layer (예: @Controller, @RestController, @ControllerAdvice)만 로드한다.
//    ㄴ @Component, @Service, @Repository 등은 기본적으로 포함되지 않는다.
//    ㄴ 따라서 내가 직접 구현한 Security 설정도 포함되지 않는다.
//  ㄴ @WebMvcTest 는 @AutoConfigureMockMvc(addFilters = true)를 자동으로 포함하는데, 이는 시큐리티 기본 필터(기본 SecurityFilterChain)가 적용된다.
//    ㄴ 기본 시큐리티 필터 체인(SecurityAutoConfiguration)이 동작하기에, 기본 정책 (예: 모든 요청은 인증 필요) 이 적용된다.
//    ㄴ 여기서, 주의할 점은 내가 만든 SecurityConfig 등 커스텀 설정은 포함되지 않는다. ( 왜냐하면 @WebMvcTest는 기본적으로 그런 빈들을 로딩하지 않기 때문 )
//  ㄴ 참고로 @WebMvcTest 는 @AutoConfigureMockMvc 를 자동으로 포함하고, @AutoConfigureMockMvc의 addFilters 속성의 기본값은 true 이다.
//    ㄴ 이 속성을 사용하면 기본 보안 필터 체인(SecurityFilterChain)은 활성화된다. ( 하지만 여전히 커스텀 SecurityConfig는 적용되지 않음 (이건 따로 import 해줘야 함) )
//    ㄴ 이 속성을 false 로 하게되면, 마치 시큐리티 없는 상태처럼 동작한다.
//
// - 그러면, @WebMvcTest 에서 내가 적용한 시큐리티 적용하려면?
//  ㄴ 1) 직접 명시적으로 시큐리티 설정을 임포트해서 등록하거나, 필요한 빈만 따로 import 해준다.
//    ㄴ @Import(SecurityConfig.class) // 직접 임포트 --> 뿐만 아니라, CustomAuthenticationEntryPoint 등도 전부 직접 등록해주어야 한다.
//  ㄴ 2) 시큐리티 테스트를 통합테스트로 하는 것.
//
// 참고) 시큐리티는 시큐리티대로 별도로 테스트하고, 컨트롤러는 요청값에 대한 검증과 반환에 집중할 수도 있다.
//  ㄴ 그러나 만약, 컨트롤러에서 @AuthenticationPrincipal 등을 사용해서 인증 정보가 필요하다면?
//    ㄴ 사용하지 않는 컨트롤러는 ( @WebMvcTest + addFilters = false) 로, 필터 없이 요청 검증만 테스트하고,
//    ㄴ 인증이 필요한 컨트롤러는 ( @WebMvcTest + addFilters = true + @WithMockUser ) 로 사용할 수도 있다. ( 이경우는 통합테스트를 사용해서 편리하게 처리할 수도 있지않을까? )
//
// 참고) @WithMockUser / @WithUserDetails
//  ㄴ @WithMockUser: 단순히 인증 상태로 테스트만 하면 됨
//  ㄴ @WithUserDetails: DB의 실제 유저 정보를 기반으로 테스트하고 싶음
//
// 정답은 없어보인다. 미리 알아두고, 적절하게 처리해서 사용하자.