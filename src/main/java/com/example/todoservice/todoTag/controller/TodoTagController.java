package com.example.todoservice.todoTag.controller;

import com.example.todoservice.common.exception.ApiResponse;
import com.example.todoservice.todoTag.dto.TodoTagResponse;
import com.example.todoservice.todoTag.service.TodoTagService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TodoTagController {

    private final TodoTagService todoTagService;

    @PostMapping("/todos/{id}/tags/{tagId}")
    public ResponseEntity<ApiResponse<TodoTagResponse>> registerTodoTag(
            @PathVariable Long id,
            @PathVariable Long tagId,
            HttpServletRequest request
    ) {
        Long memberId = (Long) request.getAttribute("memberId");
        TodoTagResponse todoTagResponse = todoTagService.addTagToTodo(id, tagId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(todoTagResponse));
    }
}
