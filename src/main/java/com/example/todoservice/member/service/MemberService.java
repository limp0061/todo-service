package com.example.todoservice.member.service;

import com.example.todoservice.member.domain.Member;
import com.example.todoservice.member.dto.MemberSaveResponse;
import com.example.todoservice.member.dto.MemberSaveRequest;
import com.example.todoservice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberValidator memberValidator;
    private final MemberRepository memberRepository;

    @Transactional
    public MemberSaveResponse registerMember(MemberSaveRequest request) {

        memberValidator.validateLoginId(request.loginId());

        Member member = request.toEntity();
        memberRepository.save(member);

        return MemberSaveResponse.from(member);
    }
}
