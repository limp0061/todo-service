package com.example.todoservice.member.service;

import com.example.todoservice.common.exception.BusinessException;
import com.example.todoservice.member.exception.MemberErrorCode;
import com.example.todoservice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;

    public void validateLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new BusinessException(MemberErrorCode.DUPLICATE_LOGIN_ID);
        }
    }
}
