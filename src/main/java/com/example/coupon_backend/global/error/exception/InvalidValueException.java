package com.example.coupon_backend.global.error.exception;

import com.example.coupon_backend.global.error.code.ErrorCode;
import com.example.coupon_backend.global.error.code.CommonErrorCode;

public class InvalidValueException extends BusinessException {

    public InvalidValueException(String value) {
        super(value, CommonErrorCode.INVALID_INPUT_VALUE);
    }

    public InvalidValueException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}