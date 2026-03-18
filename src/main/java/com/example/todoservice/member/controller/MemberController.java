package com.example.todoservice.member.controller;

import com.example.todoservice.common.exception.ApiResponse;
import com.example.todoservice.member.dto.MemberSaveRequest;
import com.example.todoservice.member.dto.MemberSaveResponse;
import com.example.todoservice.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberSaveResponse>> registerMember(
            @Valid @RequestBody MemberSaveRequest memberSaveRequest
    ) {
        MemberSaveResponse response = memberService.registerMember(memberSaveRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }
}
