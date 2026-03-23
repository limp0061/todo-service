package com.example.todoservice.tag.dto;

import com.example.todoservice.member.domain.Member;
import com.example.todoservice.tag.domain.Tag;
import jakarta.validation.constraints.NotBlank;

public record TagSaveRequest(

        @NotBlank(message = "태그 이름을 입력해주세요")
        String name,

        @NotBlank(message = "태그 색을 선택해주세요")
        String color
) {

    public Tag toEntity(Member member) {
        return Tag.builder()
                .member(member)
                .name(name)
                .color(color)
                .build();
    }
}
