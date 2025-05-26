package com.example.coupon_backend.domain.member.repository;

import com.example.coupon_backend.domain.member.entity.Member;
import com.example.coupon_backend.domain.member.enums.MemberStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.coupon_backend.domain.member.entity.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Member> findMembers(Pageable pageable, String name, MemberStatus status) {

        List<Member> content = jpaQueryFactory
                .selectFrom(member)
                .where(
                        nameLike(name),
                        statusEq(status)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(member.count())
                .from(member)
                .where(
                        nameLike(name),
                        statusEq(status)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression nameLike(String name) {
        return StringUtils.hasText(name) ? member.name.contains(name.trim()) : null;
    }

    private BooleanExpression statusEq(MemberStatus status) {
        return status != null ? member.status.eq(status) : null;
    }

}
