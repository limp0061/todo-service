package com.example.todoservice.todo.dto;

import com.example.todoservice.tag.dto.TagResponse;
import com.example.todoservice.todo.domain.Priority;
import com.example.todoservice.todo.domain.RepeatType;
import com.example.todoservice.todo.domain.Todo;
import com.example.todoservice.todo.domain.TodoStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TodoDetailResponse(
        Long id,
        String title,
        String description,
        LocalDate scheduledDate,
        LocalDate dueDate,
        Priority priority,
        TodoStatus status,
        RepeatType repeatType,
        LocalDate repeatEndDate,
        List<TagResponse> tags,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt
) {
    public static TodoDetailResponse of(Todo todo, List<TagResponse> tags) {
        return new TodoDetailResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getScheduledDate(),
                todo.getDueDate(),
                todo.getPriority(),
                todo.getStatus(),
                todo.getRepeatType(),
                todo.getRepeatEndDate(),
                tags,
                todo.getCreatedAt()
        );
    }
}
