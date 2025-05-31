package com.example.coupon_backend.domain.member.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginRequest {
    private String username;
    private String password;
}
