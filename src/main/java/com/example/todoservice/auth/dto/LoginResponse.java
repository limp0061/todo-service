package com.example.todoservice.auth.dto;

import com.example.todoservice.member.domain.Member;

public record LoginResponse(
        String loginId,
        String name,
        String accessToken,
        String refreshToken
) {

    public static LoginResponse of(Member member, String accessToken, String refreshToken) {
        return new LoginResponse(member.getLoginId(), member.getName(), accessToken, refreshToken);
    }
}
