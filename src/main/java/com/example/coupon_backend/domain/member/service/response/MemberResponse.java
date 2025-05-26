package com.example.coupon_backend.domain.member.service.response;

import com.example.coupon_backend.domain.member.entity.Member;
import com.example.coupon_backend.domain.member.enums.MemberStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class MemberResponse {
    private Long id;
    private String name;
    private MemberStatus status;
    private LocalDateTime joinDt;

    public static MemberResponse of(final Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getStatus(),
                member.getCreatedAt());
    }
}