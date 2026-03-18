package com.example.todoservice.common.utils;

import com.example.todoservice.auth.exception.AuthErrorCode;
import com.example.todoservice.common.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long accessTokenExpire;
    private final long refreshTokenExpire;
    private final String issuer;

    public JwtUtil(
            @Value("${spring.jwt.secret}") String secretKey,
            @Value("${spring.jwt.token.access-expiration}") long accessTokenExpire,
            @Value("${spring.jwt.token.refresh-expiration}") long refreshTokenExpire,
            @Value("${spring.application.name}") String issuer
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenExpire = accessTokenExpire;
        this.refreshTokenExpire = refreshTokenExpire;
        this.issuer = issuer;
    }

    public String createToken(Long memberId) {
        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpire))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(Long memberId) {
        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpire))
                .signWith(secretKey)
                .compact();
    }

    public Long getMemberId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return Long.parseLong(claims.getSubject());
        } catch (ExpiredJwtException e) {
            throw new BusinessException(AuthErrorCode.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new BusinessException(AuthErrorCode.INVALID_TOKEN);
        }
    }
}
