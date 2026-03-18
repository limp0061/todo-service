package com.example.todoservice.common.exception;

public record FieldErrorDetail(
        String field,
        String reason
) {
}
