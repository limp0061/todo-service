package com.example.todoservice.tag.controller;

import com.example.todoservice.common.annotation.LoginId;
import com.example.todoservice.common.exception.ApiResponse;
import com.example.todoservice.tag.dto.TagResponse;
import com.example.todoservice.tag.dto.TagSaveRequest;
import com.example.todoservice.tag.dto.TagUpdateRequest;
import com.example.todoservice.tag.service.TagService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
            @LoginId Long memberId
    ) {
        TagResponse tagResponse = tagService.registerTag(tagSaveRequest, memberId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(tagResponse)
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TagResponse>>> getAllTags(
            @LoginId Long memberId
    ) {
        List<TagResponse> allTags = tagService.getAllTags(memberId);
        return ResponseEntity.ok(ApiResponse.success(allTags));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<TagResponse>> updateTag(
            @PathVariable Long id,
            @Valid @RequestBody TagUpdateRequest tagUpdateRequest,
            @LoginId Long memberId
    ) {
        TagResponse tagResponse = tagService.updateTag(tagUpdateRequest, id, memberId);
        return ResponseEntity.ok((ApiResponse.success(tagResponse)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTag(
            @PathVariable Long id,
            @LoginId Long memberId
    ) {
        tagService.deleteTag(id, memberId);

        return ResponseEntity.noContent().build();
    }
}
