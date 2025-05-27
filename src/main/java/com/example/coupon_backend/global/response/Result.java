package com.example.coupon_backend.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class Result<T> {
    private int status = HttpStatus.OK.value();
    private T data;

    private Result(T data) {
        this.data = data;
    }

    public static <T> Result<T> of(T t) {
        return new Result<>(t);
    }
}
