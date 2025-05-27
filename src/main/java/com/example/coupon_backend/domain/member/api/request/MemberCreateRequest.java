package com.example.coupon_backend.domain.member.api.request;

import com.example.coupon_backend.domain.member.service.request.MemberCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCreateRequest {

    @NotBlank(message = "아이디는 필수입니다.")
    private String id;

    @NotBlank(message = "회원 이름은 필수입니다.")
    private String name;

    @Builder
    private MemberCreateRequest(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public MemberCreateServiceRequest toServiceRequest() {
        return MemberCreateServiceRequest.builder()
                .id(id)
                .name(name)
                .build();
    }
}
