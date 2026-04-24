package com.example.coupon_backend.global.config.security;

import com.example.coupon_backend.domain.member.enums.MemberRole;
import com.example.coupon_backend.global.config.security.handler.CustomAccessDeniedHandler;
import com.example.coupon_backend.global.config.security.handler.CustomAuthenticationEntryPoint;
import com.example.coupon_backend.global.config.security.jwt.JwtAuthenticationFilter;
import com.example.coupon_backend.global.config.security.jwt.JwtAuthorizationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * TODO :: 로그아웃
 * TODO :: Redis 연동 (refresh_token)
 *  ㄴ Access Token: 클라이언트가 들고 있고, 검증만 함 -> 서버에서 삭제/등록 안함
 *  ㄴ Refresh Token: Redis에서 관리 -> Access Token 재발급과 로그아웃 처리에 사용
 */
@Slf4j
@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        log.debug("BCryptPasswordEncoder bean creation");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           CustomAuthenticationEntryPoint authenticationEntryPoint,
                                           CustomAccessDeniedHandler accessDeniedHandler,
                                           AuthenticationManager authenticationManager) throws Exception {
        log.debug("SecurityFilterChain bean creation");

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager);
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(configurationSource()))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함
                .formLogin(AbstractHttpConfigurer::disable) // 기본 폼 로그인 필터 비활성화 ( 기본 /login 비활성화 ) | 참고. formLogin().disable()은 Spring Security의 기본 폼 로그인 기능과 그에 연결된 UsernamePasswordAuthenticationFilter를 비활성화하는 것. 따라서 사용자가 /login으로 요청해도 인증 시도가 발생하지 않음  ( 결국 아래 설정에 의해서 /api/login 에서만 인증 처리 )
                .httpBasic(AbstractHttpConfigurer::disable) // 기본인증 끄기
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(authorizeRequest -> authorizeRequest
                        .requestMatchers("/api/members/signup", "/api/login").anonymous() // anonymous()는 인증 객체가 없는 상태에서만 접근 가능하므로 로그인된 사용자가 접근하면 403 Forbidden 이 발생
                        .requestMatchers("/api/members/**").authenticated()
                        .requestMatchers("/api/bills/**").authenticated()
                        .requestMatchers("/api/order/**").hasRole(MemberRole.VIP.name())
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // 앞에있는 필터가 뒤 필터보다 먼저 수행되도록 설정
                .addFilterBefore(jwtAuthorizationFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        log.debug("CorsConfigurationSource registration in SecurityFilterChain");
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
