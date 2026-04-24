package com.example.coupon_backend.domain.member.api;

import com.example.coupon_backend.domain.member.api.request.MemberCreateRequest;
import com.example.coupon_backend.domain.member.api.request.MemberSearchRequest;
import com.example.coupon_backend.domain.member.service.MemberService;
import com.example.coupon_backend.domain.member.service.response.MemberResponse;
import com.example.coupon_backend.global.api.ApiResponse;
import com.example.coupon_backend.global.config.security.service.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 조회
     *
     * - /api/members?page=0&size=5&name=kim&status=NORMAL
     */
    @GetMapping
    public ApiResponse<Page<MemberResponse>> getMembers(
            Pageable pageable,
            @ModelAttribute MemberSearchRequest request
    ) {
        Page<MemberResponse> members = memberService.getMembers(pageable, request.getName(), request.getStatus());
        return ApiResponse.ok(members);
    }

    /**
     * 회원 상세 조회
     */
    @GetMapping("/info")
    public ApiResponse<MemberResponse> getMember() {
        Long userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember().getId(); // id를 파라미터로 받으면 다른 유저 정보도 볼수있으니, SecurityContextHolder 에서 조회하도록.  ==>  TODO:: 이런 코드는 반복되는 보일러플레이트 코드가 될 가능성 높다. 따라서 컨트롤러에서 간편하게 파라미터로 받을 수 있도록 애노테이션 기반으로 ArgumentResolver 처리할 것.
        return ApiResponse.ok(memberService.getMember(userId));
    }

    /**
     * 회원 등록
     */
    @PostMapping("/signup")
    public ApiResponse<MemberResponse> createMember(
            @RequestBody @Valid MemberCreateRequest memberCreateRequest
    ) {
        Long savedId = memberService.save(memberCreateRequest.toServiceRequest());
        return ApiResponse.ok(memberService.getMember(savedId));
    }

}
