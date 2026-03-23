package com.example.todoservice.todoTag.dto;

import com.example.todoservice.todoTag.domain.TodoTag;

public record TodoTagResponse(
        Long todoTagId
) {
    public static TodoTagResponse from(TodoTag todoTag) {
        return new TodoTagResponse(todoTag.getId());
    }
}
