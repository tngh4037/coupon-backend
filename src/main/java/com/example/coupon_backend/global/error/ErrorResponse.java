package com.example.coupon_backend.global.error;

import com.example.coupon_backend.global.error.code.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String message;
    private String code;
    private int status;
    private List<FieldError> errors;

    private ErrorResponse(final ErrorCode code) {
        this.code = code.getCode();
        this.message = code.getMessage();
        this.status = code.getHttpStatus().value();
        this.errors = new ArrayList<>();
    }

    private ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
        this.code = code.getCode();
        this.message = code.getMessage();
        this.status = code.getHttpStatus().value();
        this.errors = errors;
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final ErrorCode code, final List<FieldError> errors) {
        return new ErrorResponse(code, errors);
    }

    @Getter
    public static class FieldError {
        private final String field;
        private final String value;
        private final String reason;

        public FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }
    }

}
