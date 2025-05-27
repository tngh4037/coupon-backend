package com.example.coupon_backend.domain.member.api.request;

import com.example.coupon_backend.domain.member.enums.MemberStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSearchRequest {
    private String name;
    private MemberStatus status;
}