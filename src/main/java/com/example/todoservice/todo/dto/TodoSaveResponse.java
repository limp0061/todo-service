package com.example.todoservice.todo.dto;

import com.example.todoservice.todo.domain.Priority;
import com.example.todoservice.todo.domain.RepeatType;
import com.example.todoservice.todo.domain.Todo;
import com.example.todoservice.todo.domain.TodoStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TodoSaveResponse(
        Long id,
        String title,
        String description,
        LocalDate scheduledDate,
        LocalDate dueDate,
        Priority priority,
        TodoStatus status,
        RepeatType repeatType,
        LocalDate repeatEndDate,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt
) {
    public static TodoSaveResponse from(Todo todo) {
        return new TodoSaveResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getScheduledDate(),
                todo.getDueDate(),
                todo.getPriority(),
                todo.getStatus(),
                todo.getRepeatType(),
                todo.getRepeatEndDate(),
                todo.getCreatedAt()
        );
    }
}
