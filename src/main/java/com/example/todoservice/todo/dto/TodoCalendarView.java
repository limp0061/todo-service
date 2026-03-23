package com.example.todoservice.todo.dto;

import java.time.LocalDate;

public record TodoCalendarView(
        LocalDate date,
        Long count
) {
}
