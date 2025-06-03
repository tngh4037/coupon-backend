package com.example.coupon_backend.global.config.security.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginRequest {
    private String username;
    private String password;
}
