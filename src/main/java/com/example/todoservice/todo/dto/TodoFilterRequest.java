package com.example.todoservice.todo.dto;

import com.example.todoservice.todo.domain.Priority;
import com.example.todoservice.todo.domain.TodoStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
public class TodoFilterRequest {

    @NotNull(message = "시작 일을 입력해주세요")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @NotNull(message = "종료 일을 입력해주세요")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    private TodoStatus status;
    private Priority priority;
    private Long tagId;
}
