package com.example.todoservice.tag.controller;

import com.example.todoservice.common.exception.ApiResponse;
import com.example.todoservice.tag.dto.TagResponse;
import com.example.todoservice.tag.dto.TagSaveRequest;
import com.example.todoservice.tag.service.TagService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<ApiResponse<TagResponse>> registerTag(
            @Valid @RequestBody TagSaveRequest tagSaveRequest,
            HttpServletRequest request
    ) {
        Long memberId = (Long) request.getAttribute("memberId");

        TagResponse tagResponse = tagService.registerTag(tagSaveRequest, memberId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(tagResponse)
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TagResponse>>> getAllTags(
            HttpServletRequest request
    ) {
        Long memberId = (Long) request.getAttribute("memberId");
        List<TagResponse> allTags = tagService.getAllTags(memberId);
        return ResponseEntity.ok(ApiResponse.success(allTags));
    }
}
