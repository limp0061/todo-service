package com.example.todoservice.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
