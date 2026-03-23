package com.example.todoservice.todo.controller;

import com.example.todoservice.common.exception.ApiResponse;
import com.example.todoservice.todo.dto.RepeatScope;
import com.example.todoservice.todo.dto.TodoDetailResponse;
import com.example.todoservice.todo.dto.TodoFilterRequest;
import com.example.todoservice.todo.dto.TodoSaveRequest;
import com.example.todoservice.todo.dto.TodoSaveResponse;
import com.example.todoservice.todo.dto.TodoUpdateRequest;
import com.example.todoservice.todo.service.TodoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<ApiResponse<TodoSaveResponse>> registerTodo(
            @Valid @RequestBody TodoSaveRequest todoSaveRequest,
            HttpServletRequest request
    ) {
        Long memberId = (Long) request.getAttribute("memberId");
        TodoSaveResponse todoSaveResponse = todoService.registerTodos(todoSaveRequest, memberId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(todoSaveResponse)
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TodoDetailResponse>>> getTodos(
            @Valid @ModelAttribute TodoFilterRequest todoFilterRequest,
            HttpServletRequest request
    ) {
        Long memberId = (Long) request.getAttribute("memberId");
        List<TodoDetailResponse> todos = todoService.getTodos(todoFilterRequest, memberId);
        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(todos)
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoDetailResponse>> getTodoDetail(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        Long memberId = (Long) request.getAttribute("memberId");
        TodoDetailResponse todoDetail = todoService.getTodoDetail(id, memberId);

        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(todoDetail)
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoSaveResponse>> updateTodo(
            @PathVariable Long id,
            @Valid @RequestBody TodoUpdateRequest updateRequest,
            HttpServletRequest request
    ) {
        Long memberId = (Long) request.getAttribute("memberId");
        TodoSaveResponse todoSaveResponse = todoService.updateTodo(updateRequest, id, memberId);
        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(todoSaveResponse)
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTodo(
            @PathVariable Long id,
            @RequestParam RepeatScope scope,
            HttpServletRequest request
    ) {
        Long memberId = (Long) request.getAttribute("memberId");
        todoService.deleteTodo(id, memberId, scope);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<TodoSaveResponse>> completeTodo(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        Long memberId = (Long) request.getAttribute("memberId");
        TodoSaveResponse todoSaveResponse = todoService.completeTodo(id, memberId);
        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(todoSaveResponse)
                );
    }
}
