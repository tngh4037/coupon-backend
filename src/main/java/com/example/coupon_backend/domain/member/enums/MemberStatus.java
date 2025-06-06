package com.example.coupon_backend.domain.member.enums;

import com.example.coupon_backend.global.enums.BaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus implements BaseEnum {
    NORMAL("정상"), 
    STOP("중지"), 
    SLEEP("휴면"), 
    QUIT("탈퇴")
    ;

    private final String title;
}
