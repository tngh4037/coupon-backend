package com.example.coupon_backend.domain.bill.enums;

import com.example.coupon_backend.global.enums.BaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BillType implements BaseEnum {
    BUY("일반결제"),
    EVENT("이벤트결제"),
    CHARGE("충전결제"),
    ;

    private final String title;

    public boolean isPurchaseType() {
        return this == BUY || this == EVENT;
    }
}
