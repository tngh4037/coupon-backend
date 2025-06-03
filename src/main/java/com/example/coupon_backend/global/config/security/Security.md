### JwtAuthenticationFilter(extends UsernamePasswordAuthenticationFilter) - 로그인 과정에서 인증 수행
- /api/login 에서만 동작
- [인증 과정 동작 흐름]
  - JwtAuthenticationFilter.attemptAuthentication()
  - AuthenticationManager.authenticate()
    - 내부에서 Provider 및 UserDetailsService.loadUserByUsername() 호출 ( 참고. AuthenticationManager는 내부적으로 DaoAuthenticationProvider 등을 통해 비밀번호 검증을 수행 )
    - Authentication 객체에 UserDetails 를 저장하고 SecurityContextHolder 에 보관
    - Authentication 리턴
- [인증 성공시 successfulAuthentication]
  - 인자로 넘어온 Authentication 를 기반으로 JWT 토큰 생성해서 클라이언트 응답 헤더에 반환
- [인증 실패시 unsuccessfulAuthentication]
  - 적절한 예외 정보를 클라이언트에 반환

&nbsp;

### JwtAuthorizationFilter(extends BasicAuthenticationFilter) - 모든 경로에서 [인증/인가] 필터링
- JWT 토큰 기반으로 모든 요청에 대해 인증/인가 수행
- [인증/인가 처리 과정 흐름]
  - JwtAuthorizationFilter.doFilterInternal
    - 내부에서 Jwt Token 검증
    - 토큰 검증 후 Authentication 객체를 생성해서 직접 SecurityContextHolder 에 객체를 저장. ( 구조: SecurityContextHolder(Authentication(CustomUserDetails)) )
      - 이렇게 직접 넣어주는 이유는, 이후 스프링 시큐리티의 인증/인가 처리의 편의 도움을 받기 위해서임. ( 이렇게 하면, 이후 시큐리티가 인증 여부(isAuthenticated())와 권한(hasRole, @PreAuthorize)을 판단하는 데 사용된다. )
        - SecurityContextHolder(Authentication(CustomUserDetails)): 인증은 됐구나 판단 ( ex. authenticated(), permitAll 판단 기준 )
          - SecurityContextHolder가 Authentication(인증 객체)을 갖고 있고, isAuthenticated()가 true면 인증된 사용자로 간주
        - Authentication 안에 있는 role 기준으로 인가 검증 판단 (ex. hasRole, @PreAuthorize 판단 기준 )
          - 인가 판단 시에는 Authentication.getAuthorities()에 담긴 권한 정보를 기준으로 hasRole, hasAuthority, @PreAuthorize("hasRole('ROLE_ADMIN')") 같은 어노테이션 기반 인가가 이루어진다.
- [[인증]에서 유효성 실패시 핸들링: CustomAuthenticationEntryPoint]
- [[인가]에서 유효성 실패시 핸들링: CustomAccessDeniedHandler]


&nbsp;

### 참고) 요청 -> 응답 중에 ThreadLocal을 통해 인증 정보가 일시 저장
- sessionManagement 를 SessionCreationPolicy.STATELESS 로 했기 때문에, 요청 처리 중에만 ThreadLocal에 일시 저장
- (요청 -> 응답)시 일시적으로 인증/인가 검증을 위해 사용하고, 응답이 나가면 사라진다.
  - Spring Security는 요청을 처리하는 동안 SecurityContextHolder의 ThreadLocal 저장소를 사용한다.
  - 이 저장소는 요청이 들어오면 초기화되고, 요청이 끝나면 정리된다.
  - 따라서 JWT 인증 정보는 “세션”이 아니라 “현재 요청 스레드에 일시적으로 저장”되는 구조


&nbsp;

### 참고) SessionCreationPolicy.STATELESS
- 세션을 생성하거나 저장하지 않음 (HttpSession을 만들지 않음)
- JSESSIONID 쿠키를 보내지 않음
- 인증 정보를 HttpSession에 저장하지 않음
