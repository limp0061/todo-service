package com.example.todoservice.auth.filter;

import com.example.todoservice.auth.exception.AuthErrorCode;
import com.example.todoservice.common.exception.BusinessException;
import com.example.todoservice.common.exception.ErrorCode;
import com.example.todoservice.common.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 화이트리스트 (인증이 아예 필요 없는 경로)
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/members/signup")
                || requestURI.startsWith("/api/auth/login")
                || requestURI.startsWith("/api/auth/reissue") // RefreshToken 검증
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Authorization 헤더 확인
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            sendError(response, AuthErrorCode.TOKEN_NOT_FOUND);
            return;
        }

        try {
            String token = authorization.replace("Bearer ", "");

            // Access Token 검증 및 정보 추출
            Long memberId = jwtUtil.getMemberId(token);
            request.setAttribute("memberId", memberId);

            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            sendError(response, e.getErrorCode());
        }

    }

    private void sendError(HttpServletResponse response, ErrorCode errorCode)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                String.format(
                        "{\"success\":false,\"data\":null,\"message\":\"%s\",\"code\":\"%s\"}",
                        errorCode.getMessage(), errorCode.name()
                )
        );
    }
}
