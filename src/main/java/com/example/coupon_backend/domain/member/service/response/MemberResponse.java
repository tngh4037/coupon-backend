package com.example.coupon_backend.domain.member.service.response;

import com.example.coupon_backend.domain.member.entity.Member;
import com.example.coupon_backend.domain.member.enums.MemberStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberResponse {
    private Long id;
    private String memberId;
    private String name;
    private String email;
    private MemberStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Builder
    private MemberResponse(Long id, String memberId, String name, String email, MemberStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static MemberResponse of(final Member member) {
        return new MemberResponse(
                member.getId(),
                member.getMemberId(),
                member.getName(),
                member.getEmail(),
                member.getStatus(),
                member.getCreatedAt());
    }
}