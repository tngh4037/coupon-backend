package com.example.coupon_backend.global.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {
    private int status;
    private T data;

    private ApiResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> ApiResponse<T> ok(T t) {
        return new ApiResponse<>(HttpStatus.OK.value(), t);
    }
}
