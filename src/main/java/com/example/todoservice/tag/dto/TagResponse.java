package com.example.todoservice.tag.dto;

import com.example.todoservice.tag.domain.Tag;

public record TagResponse(
        Long id,
        String name,
        String color
) {
    public static TagResponse from(Tag tag) {
        return new TagResponse(tag.getId(), tag.getName(), tag.getColor());
    }
}
