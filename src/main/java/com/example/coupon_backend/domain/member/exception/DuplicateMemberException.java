package com.example.coupon_backend.domain.member.exception;

import com.example.coupon_backend.global.error.code.ErrorCode;
import com.example.coupon_backend.global.error.code.member.MemberErrorCode;
import com.example.coupon_backend.global.error.exception.BusinessException;

public class DuplicateMemberException extends BusinessException {

    private static final ErrorCode errorCode = MemberErrorCode.DUPLICATE_MEMBER;

    public DuplicateMemberException() {
        super(errorCode);
    }

    public DuplicateMemberException(String message) {
        super(message, errorCode);
    }
}
