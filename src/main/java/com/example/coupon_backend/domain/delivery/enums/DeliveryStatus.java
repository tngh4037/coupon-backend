package com.example.coupon_backend.domain.delivery.enums;

import com.example.coupon_backend.global.enums.BaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus implements BaseEnum {
    WAIT("배송대기"),
    COMPLETED("배송완료"),
    CANCEL("배송취소"),
    ;

    private final String title;
}

