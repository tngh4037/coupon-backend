package com.example.coupon_backend.domain.member.enums;

import com.example.coupon_backend.global.enums.BaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole implements BaseEnum {
    NORMAL("일반"),
    GOLD("골드"),
    VIP("VIP")
    ;

    private final String title;
}
