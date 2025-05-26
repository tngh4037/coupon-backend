package com.example.coupon_backend.domain.member.api;

import com.example.coupon_backend.domain.member.api.request.MemberCreateRequest;
import com.example.coupon_backend.domain.member.api.request.MemberSearchRequest;
import com.example.coupon_backend.domain.member.service.MemberService;
import com.example.coupon_backend.domain.member.service.response.MemberResponse;
import com.example.coupon_backend.global.dto.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 조회
     *
     * - /api/members?page=0&size=5&name=kim&status=NORMAL
     */
    @GetMapping("/api/members")
    public Result<Page<MemberResponse>> getMembers(
            Pageable pageable,
            @ModelAttribute MemberSearchRequest request
    ) {
        Page<MemberResponse> members = memberService.getMembers(pageable, request.getName(), request.getStatus());
        return Result.of(members);
    }

    /**
     * 회원 상세 조회
     */
    @GetMapping("/api/members/{id}")
    public Result<MemberResponse> getMember(
            @PathVariable("id") final Long id
    ) {
        return Result.of(memberService.getMember(id));
    }

    /**
     * 회원 등록
     */
    @PostMapping("/api/members")
    public Result<Long> create(
            @RequestBody @Valid MemberCreateRequest memberCreateRequest
    ) {
        return Result.of(memberService.save(memberCreateRequest.toServiceRequest()));
    }

}
