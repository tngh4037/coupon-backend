package com.example.coupon_backend.domain.bill.enums;

import com.example.coupon_backend.global.enums.BaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BillStatus implements BaseEnum {
    WAIT("결제대기"), 
    COMPLETED("결제완료"),
    FAILED("결제실패"),
    CANCELED("결제취소"),
    ;

    private final String title;
}
