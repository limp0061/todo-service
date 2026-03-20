package com.example.todoservice.member.dto;

import com.example.todoservice.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record MemberSaveResponse(
        Long id,
        String loginId,
        String name,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt
) {

    public static MemberSaveResponse from(Member member) {
        return new MemberSaveResponse(
                member.getId(),
                member.getLoginId(),
                member.getName(),
                member.getCreatedAt()
        );
    }
}
