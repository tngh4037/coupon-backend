package com.example.coupon_backend.domain.goods.enums;

import com.example.coupon_backend.global.enums.BaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GoodsType implements BaseEnum {
    B2B("기업"),
    B2C("개인"),
    ;

    private final String title;
}