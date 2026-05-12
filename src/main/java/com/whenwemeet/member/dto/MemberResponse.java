package com.whenwemeet.member.dto;

import com.whenwemeet.member.entity.Member;

import java.time.LocalDateTime;

public record MemberResponse(
        Long id,
        String loginId,
        String name,
        String provider,
        LocalDateTime createdAt
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getLoginId(),
                member.getName(),
                member.getProvider(),
                member.getCreatedAt()
        );
    }
}