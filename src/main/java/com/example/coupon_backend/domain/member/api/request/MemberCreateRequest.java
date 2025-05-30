package com.example.coupon_backend.domain.member.api.request;

import com.example.coupon_backend.domain.member.service.request.MemberCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberCreateRequest {

    @NotBlank(message = "아이디는 필수입니다.")
    private String memberId;

    @NotBlank(message = "패스워드는 필수입니다.")
    private String password;

    @NotBlank(message = "회원 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @Builder
    private MemberCreateRequest(String memberId, String password, String name, String email) {
        this.memberId = memberId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public MemberCreateServiceRequest toServiceRequest() {
        return MemberCreateServiceRequest.builder()
                .memberId(memberId)
                .password(password)
                .name(name)
                .email(email)
                .build();
    }
}
