package com.example.todoservice.member.dto;

import com.example.todoservice.common.annotation.ValidLoginId;
import com.example.todoservice.common.annotation.ValidPassword;
import com.example.todoservice.member.domain.Member;
import jakarta.validation.constraints.NotBlank;

public record MemberSaveRequest(

        @ValidLoginId
        String loginId,

        @ValidPassword
        String password,

        @NotBlank
        String name
) {
    public Member toEntity() {
        return Member.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .build();
    }
}
