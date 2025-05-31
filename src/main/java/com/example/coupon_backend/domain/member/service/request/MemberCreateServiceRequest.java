package com.example.coupon_backend.domain.member.service.request;

import com.example.coupon_backend.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberCreateServiceRequest {

    private String memberId;
    private String password;
    private String name;
    private String email;

    @Builder
    public MemberCreateServiceRequest(String memberId, String password, String email, String name) {
        this.memberId = memberId;
        this.password = password;
        this.email = email;
        this.name = name;
    }

    public Member toEntity() {
        return Member.join(memberId, password, email, name);
    }
}
