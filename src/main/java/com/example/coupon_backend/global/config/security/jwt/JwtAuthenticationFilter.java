package com.example.coupon_backend.global.config.security.jwt;

import com.example.coupon_backend.domain.member.api.request.MemberLoginRequest;
import com.example.coupon_backend.domain.member.api.request.MemberLoginResponse;
import com.example.coupon_backend.global.api.ApiResponse;
import com.example.coupon_backend.global.config.security.service.CustomUserDetails;
import com.example.coupon_backend.global.error.ErrorResponse;
import com.example.coupon_backend.global.error.code.CommonErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

// "/api/login" 에서만 동작
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login");
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        log.debug("attemptAuthentication called");

        try {

            ObjectMapper om = new ObjectMapper();
            MemberLoginRequest loginRequest = om.readValue(request.getReader(), MemberLoginRequest.class);

            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            if (username == null || username.isBlank()) {
                throw new BadCredentialsException("Username is required");
            }
            if (password == null || password.isBlank()) {
                throw new BadCredentialsException("Password is required");
            }

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);

            // authenticate() 내부에서 UserDetailsService 를 호출 -> 해당 메서드는 UserDetails 를 리턴함 -> 시큐리티는 내부에서는 Authentication(인증 객체) 생성하여 그 안에 UserDetails 를 넣어두고, Authentication(인증 객체)를 SecurityContextHolder에 넣어둠 -> 그리고 authenticate() 메서드의 리턴값으로 Authentication 를 반환해 줌.
            Authentication authentication = authenticationManager.authenticate(authenticationToken); // 참고) authenticate() 내부에서 비밀번호 검증이 이뤄짐 ( by Provider )
            return authentication;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        }
    }

    // 인증 성공
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.debug("successfulAuthentication called");

        // header
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        String jwtToken = JwtTokenProvider.create(userDetails);
        response.addHeader(JwtTokenProvider.getHeader(), jwtToken);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());

        // body
        ObjectMapper om = new ObjectMapper();
        MemberLoginResponse loginResponse = new MemberLoginResponse(userDetails.getMember());
        ApiResponse<MemberLoginResponse> apiResponse = ApiResponse.ok(loginResponse);
        String responseBody = om.writeValueAsString(apiResponse);
        response.getWriter().println(responseBody);
    }

    // 인증 실패
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.debug("Authentication failed: {}", failed.getMessage());

        // header
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // body
        ObjectMapper om = new ObjectMapper();
        ErrorResponse errorResponse = ErrorResponse.of(CommonErrorCode.LOGIN_FAIL);
        String responseBody = om.writeValueAsString(errorResponse);
        response.getWriter().println(responseBody);
    }
}

/**
 * - authenticate() 호출 시 내부적으로 UserDetailsService.loadUserByUsername() 호출
 * - DB에서 사용자 정보 조회 후 UserDetails 생성
 * - DaoAuthenticationProvider가 입력된 비밀번호와 DB 비밀번호(암호화된 값)를 비교하여 검증
 * - 검증 성공 시, UserDetails를 포함한 Authentication 객체 생성 및 반환
 * -  ㄴ 이때 Spring Security는 반환된 Authentication을 SecurityContextHolder에 저장해 인증 상태 유지
 */