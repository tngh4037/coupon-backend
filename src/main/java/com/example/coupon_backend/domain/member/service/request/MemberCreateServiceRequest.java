package com.example.coupon_backend.domain.member.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberCreateServiceRequest {

    private String id;
    private String name;

    @Builder
    public MemberCreateServiceRequest(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
