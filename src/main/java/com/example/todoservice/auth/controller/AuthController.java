package com.example.todoservice.auth.controller;

import com.example.todoservice.auth.dto.LoginRequest;
import com.example.todoservice.auth.dto.LoginResponse;
import com.example.todoservice.auth.dto.TokenResponse;
import com.example.todoservice.auth.service.AuthService;
import com.example.todoservice.common.exception.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        LoginResponse response = authService.authLogin(loginRequest);
        return ResponseEntity.ok().body(
                ApiResponse.success(response)
        );
    }

    @GetMapping("/reissue")
    public ResponseEntity<ApiResponse<TokenResponse>> reissue(
            HttpServletRequest request
    ) {
        TokenResponse response = authService.refreshToken(request);
        return ResponseEntity.ok().body(
                ApiResponse.success(response)
        );
    }

}
