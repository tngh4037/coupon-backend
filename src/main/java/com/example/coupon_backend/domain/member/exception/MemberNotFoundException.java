package com.example.coupon_backend.domain.member.exception;

import com.example.coupon_backend.global.error.code.ErrorCode;
import com.example.coupon_backend.global.error.code.member.MemberErrorCode;
import com.example.coupon_backend.global.error.exception.BusinessException;

public class MemberNotFoundException extends BusinessException {

    private static final ErrorCode errorCode = MemberErrorCode.MEMBER_NOT_FOUND;

    public MemberNotFoundException() {
        super(errorCode);
    }

    public MemberNotFoundException(String message) {
        super(message, errorCode);
    }
}
