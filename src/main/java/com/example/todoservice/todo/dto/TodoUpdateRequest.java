package com.example.todoservice.todo.dto;

import com.example.todoservice.todo.domain.Priority;
import com.example.todoservice.todo.domain.RepeatType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TodoUpdateRequest(

        @NotBlank(message = "제목을 입력해주세요")
        String title,
        String description,

        @NotNull(message = "시작일을 입력해주세요")
        LocalDate scheduledDate,
        LocalDate dueDate,

        @NotNull(message = "우선순위를 정해주세요")
        Priority priority,

        @NotNull(message = "반복 여부를 선택해주세요")
        RepeatType repeatType,

        LocalDate repeatEndDate,

        @NotNull(message = "")
        RepeatScope scope
) {
}
