package com.example.coupon_backend.domain.member.repository;

import com.example.coupon_backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    @Modifying(clearAutomatically = true)
    @Query("delete from Member m")
    int deleteAllMember();

}
