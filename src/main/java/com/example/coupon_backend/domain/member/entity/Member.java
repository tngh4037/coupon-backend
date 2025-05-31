package com.example.coupon_backend.domain.member.entity;

import com.example.coupon_backend.domain.member.enums.MemberRole;
import com.example.coupon_backend.domain.member.enums.MemberStatus;
import com.example.coupon_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Long id;

    private String memberId;
    private String password;
    private String email;
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_status")
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    public static Member join(String memberId, String password, String email, String name) {
        return Member.builder()
                .memberId(memberId)
                .password(password)
                .email(email)
                .name(name)
                .status(MemberStatus.NORMAL)
                .role(MemberRole.NORMAL)
                .build();
    }
}
