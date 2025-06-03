package com.example.coupon_backend.global.config.security.jwt;

import com.example.coupon_backend.global.config.security.service.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

// 모든 경로에서 동작 (토큰 검증)
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        // 참고) JWT 토큰 헤더가 없어도 해당 필터를 통과할 수 있다. 하지만, 해당 요청 경로가 인증/권한 필요시, 결국 이후 시큐리티 검증 단에서 실패한다. ( 시큐리티가 제어하도록 위임 )
        if (isExistAuthorizationHeader(request, response)) {
            String token = request.getHeader(JwtTokenProvider.getHeader()).replace(JwtTokenProvider.getTokenPrefix(), "");
            CustomUserDetails userDetails = JwtTokenProvider.verify(token);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private boolean isExistAuthorizationHeader(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(JwtTokenProvider.getHeader());
        if (header == null || !header.startsWith(JwtTokenProvider.getTokenPrefix())) {
            return false;
        }

        return true;
    }

}