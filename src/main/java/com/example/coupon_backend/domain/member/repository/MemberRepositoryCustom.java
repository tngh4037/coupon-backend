package com.example.coupon_backend.domain.member.repository;

import com.example.coupon_backend.domain.member.entity.Member;
import com.example.coupon_backend.domain.member.enums.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
    Page<Member> findMembers(Pageable pageable, String name, MemberStatus memberStatus);
}
