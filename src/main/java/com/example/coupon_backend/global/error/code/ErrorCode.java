package com.example.coupon_backend.global.error.code;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getCode();
    String getMessage();
    HttpStatus getHttpStatus();
}