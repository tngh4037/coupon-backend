package com.example.coupon_backend.domain.member.api.request;

import com.example.coupon_backend.domain.member.service.request.MemberCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCreateRequest {

    @NotBlank
    private String id;

    @NotBlank
    private String name;

    public MemberCreateServiceRequest toServiceRequest() {
        return MemberCreateServiceRequest.builder()
                .id(id)
                .name(name)
                .build();
    }
}
