package com.example.coupon_backend.global.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    INVALID_INPUT_VALUE("C001", "유효하지 않은 요청입니다.", HttpStatus.BAD_REQUEST),
    INVALID_TYPE_VALUE("C002", "유효하지 않은 요청입니다.", HttpStatus.BAD_REQUEST),
    METHOD_NOT_ALLOWED("C003", "유효하지 않은 요청입니다.", HttpStatus.METHOD_NOT_ALLOWED),
    INTERNAL_SERVER_ERROR("C004", "처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED_ERROR("C005", "로그인을 진행해 주세요.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN_ERROR("C006", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN)
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}