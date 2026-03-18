package com.example.todoservice.auth.dto;

import com.example.todoservice.common.annotation.ValidLoginId;
import com.example.todoservice.common.annotation.ValidPassword;

public record LoginRequest(
        
        @ValidLoginId
        String loginId,

        @ValidPassword
        String password
) {
}
