package com.example.coupon_backend;

import com.example.coupon_backend.domain.member.repository.MemberRepository;
import com.example.coupon_backend.domain.member.service.MemberService;
import com.example.coupon_backend.domain.member.service.request.MemberCreateServiceRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitDb {

    private final MemberSave memberSave;

    @PostConstruct
    public void init() {
        memberSave.save();
    }

    @Component
    @RequiredArgsConstructor
    static class MemberSave {

        private final MemberService memberService;
        private final MemberRepository memberRepository;
        private final BCryptPasswordEncoder passwordEncoder;

        @Transactional
        public void save() {
            memberRepository.deleteAllMember();
            IntStream.rangeClosed(1, 5)
                    .mapToObj(i -> MemberCreateServiceRequest.builder()
                            .memberId("memberId" + i)
                            .password(passwordEncoder.encode("1234"))
                            .name("kim" + i)
                            .email("test@email.com")
                            .build())
                    .forEach(memberService::save);
        }
    }
}
