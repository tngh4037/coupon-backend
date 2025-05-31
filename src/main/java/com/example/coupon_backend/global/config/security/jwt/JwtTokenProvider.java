package com.example.coupon_backend.global.config.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.coupon_backend.domain.member.entity.Member;
import com.example.coupon_backend.domain.member.enums.MemberRole;
import com.example.coupon_backend.global.config.security.service.CustomUserDetails;
import com.auth0.jwt.JWT;

import java.util.Date;

public class JwtTokenProvider {

    private static final String SUBJECT = "coupon";
    private static final String SECRET = "couponSecret2025%"; // TODO :: 코드에서 노출해선 안된다. 분리할 것.
    private static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER = "Authorization";

    public static String getHeader() {
        return HEADER;
    }

    public static String getTokenPrefix() {
        return TOKEN_PREFIX;
    }

    public static String create(CustomUserDetails userDetails) {
        String jwtToken = JWT.create()
                .withSubject(SUBJECT)
                .withClaim("id", userDetails.getMember().getId())
                .withClaim("role", userDetails.getMember().getRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET));
        return TOKEN_PREFIX + jwtToken;
    }

    public static CustomUserDetails verify(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET))
                .withSubject(SUBJECT)
                .build()
                .verify(token);

        if (decodedJWT.getClaim("id").isMissing() ||
                decodedJWT.getClaim("role").isMissing()) {
            throw new JWTVerificationException("Token is missing required claims");
        }

        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();
        Member member = Member.builder()
                .id(id)
                .role(MemberRole.valueOf(role))
                .build();

        return new CustomUserDetails(member);
    }
}
