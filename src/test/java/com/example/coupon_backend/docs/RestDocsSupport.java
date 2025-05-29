package com.example.coupon_backend.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// RestDocs 설정을 위한 상위 클래스
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsSupport {

    protected MockMvc mockMvc; // mockMvc 를 통해서 RestDoc 작성
    protected ObjectMapper objectMapper = new ObjectMapper(); // 스프링 의존성을 갖지 않을 것이므로 새로 만들어준다.

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {
        // (스프링 의존성 없이) 특정 컨트롤러를 대상으로한 MockMvc 를 만들어주고, 거기에 provider 를 통해 RestDocumentation 에 대한 설정을 넣어준다.
        this.mockMvc = MockMvcBuilders.standaloneSetup(initController()) // 참고) standaloneSetup(문서로 만들고싶은 컨트롤러) -> 모든 컨트롤러를 여기에 다 넣기에는 효율적이지 않으니, 추상메서드를 정의하고, 각 컨트롤러에서 호출해서 정보를 넣어주도록 한다.
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .build();
    }

    protected abstract Object initController();
}

// 참고)
// MockMvcBuilders.standaloneSetup(..) 을 통해 테스트할 컨트롤러(들)만 직접 등록해서 단위 테스트 수준으로 빠르게 실행할 수도 있지만,
// MockMvcBuilders.webAppContextSetup(..) 을 통해 스프링을 띄우고 mockMvc를 사용할 수도 있다. ( 애플리케이션의 전체 Spring WebApplicationContext를 로드해서 MockMvc를 초기화. | 통합 테스트, 즉 실제 애플리케이션 환경에 가까운 테스트에 적합. )