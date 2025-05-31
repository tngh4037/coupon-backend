package com.example.coupon_backend.domain.member.service;

import com.example.coupon_backend.domain.member.entity.Member;
import com.example.coupon_backend.domain.member.enums.MemberStatus;
import com.example.coupon_backend.domain.member.exception.DuplicateMemberException;
import com.example.coupon_backend.domain.member.exception.MemberNotFoundException;
import com.example.coupon_backend.domain.member.repository.MemberRepository;
import com.example.coupon_backend.domain.member.service.request.MemberCreateServiceRequest;
import com.example.coupon_backend.domain.member.service.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Page<MemberResponse> getMembers(Pageable pageable, String name, MemberStatus status) {
        Page<Member> members = memberRepository.findMembers(pageable, name, status);
        return members.map(MemberResponse::of);
    }

    public MemberResponse getMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        return MemberResponse.of(member);
    }

    @Transactional
    public Long save(MemberCreateServiceRequest request) {
        checkDuplicateMember(request.getMemberId());

        Member member = request.toEntity();
        memberRepository.save(member);
        return member.getId();
    }

    private void checkDuplicateMember(String memberId) {
        Optional<Member> findMember = memberRepository.findByMemberId(memberId);
        findMember.ifPresent(m -> {
            throw new DuplicateMemberException();
        });
    }
}
