package com.example.coupon_backend.global.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    INVALID_INPUT_VALUE("C001", "유효하지 않은 요청입니다.", HttpStatus.BAD_REQUEST),
    INVALID_TYPE_VALUE("C002", "유효하지 않은 요청입니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND("C003", "요청한 자원이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED("C004", "유효하지 않은 요청입니다.", HttpStatus.METHOD_NOT_ALLOWED),
    INTERNAL_SERVER_ERROR("C005", "처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED_ERROR("C006", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    LOGIN_FAIL("C007", "인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN_ERROR("C008", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN)
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}