package com.example.todoservice.auth.service;

import com.example.todoservice.auth.dto.LoginRequest;
import com.example.todoservice.auth.dto.LoginResponse;
import com.example.todoservice.auth.dto.TokenResponse;
import com.example.todoservice.auth.exception.AuthErrorCode;
import com.example.todoservice.common.exception.BusinessException;
import com.example.todoservice.common.redis.RedisConstants;
import com.example.todoservice.common.redis.RedisManager;
import com.example.todoservice.common.utils.JwtUtil;
import com.example.todoservice.member.domain.Member;
import com.example.todoservice.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final RedisManager redisManager;

    public LoginResponse authLogin(LoginRequest loginRequest) {

        Member member = memberRepository.findByLoginId(loginRequest.loginId())
                .orElseThrow(() -> new BusinessException(AuthErrorCode.LOGIN_FAILED));

        if (!member.getPassword().equals(loginRequest.password())) {
            throw new BusinessException(AuthErrorCode.LOGIN_FAILED);
        }

        String accessToken = jwtUtil.createToken(member.getId());
        String refreshToken = jwtUtil.createRefreshToken(member.getId());

        redisManager.set(RedisConstants.REFRESH_TOKEN_PREFIX + member.getId(), refreshToken,
                jwtUtil.getRefreshTokenExpire(),
                TimeUnit.MILLISECONDS);

        return LoginResponse.of(member, accessToken, refreshToken);
    }

    public TokenResponse refreshToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new BusinessException(AuthErrorCode.TOKEN_NOT_FOUND);
        }

        String token = authorization.replace("Bearer ", "");

        // Access Token 검증 및 정보 추출
        Long memberId = jwtUtil.getMemberId(token);
        request.setAttribute("memberId", memberId);

        String redisRefreshToken = (String) redisManager.get(RedisConstants.REFRESH_TOKEN_PREFIX + memberId);

        if (redisRefreshToken == null || !redisRefreshToken.equals(token)) {
            throw new BusinessException(AuthErrorCode.INVALID_TOKEN);
        }

        String newAccessToken = jwtUtil.createToken(memberId);
        String newRefreshToken = jwtUtil.createRefreshToken(memberId);

        redisManager.set(
                RedisConstants.REFRESH_TOKEN_PREFIX + memberId,
                newRefreshToken,
                jwtUtil.getRefreshTokenExpire(),
                TimeUnit.MILLISECONDS
        );

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
