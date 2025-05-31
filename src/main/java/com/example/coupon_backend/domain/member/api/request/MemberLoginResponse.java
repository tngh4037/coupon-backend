package com.example.coupon_backend.domain.member.api.request;

import com.example.coupon_backend.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class MemberLoginResponse {
    private Long id;
    private String username;
    private String createdAt;

    public MemberLoginResponse(Member member) {
        this.id = member.getId();
        this.username = member.getName();
        this.createdAt = member.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
