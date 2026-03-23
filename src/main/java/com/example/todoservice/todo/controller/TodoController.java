package com.example.todoservice.todo.controller;

import com.example.todoservice.common.annotation.LoginId;
import com.example.todoservice.common.exception.ApiResponse;
import com.example.todoservice.todo.dto.RepeatScope;
import com.example.todoservice.todo.dto.TodoCalendarView;
import com.example.todoservice.todo.dto.TodoDetailResponse;
import com.example.todoservice.todo.dto.TodoDoneStats;
import com.example.todoservice.todo.dto.TodoFilterRequest;
import com.example.todoservice.todo.dto.TodoSaveRequest;
import com.example.todoservice.todo.dto.TodoSaveResponse;
import com.example.todoservice.todo.dto.TodoUpdateRequest;
import com.example.todoservice.todo.service.TodoService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
            @LoginId Long memberId
    ) {
        TodoSaveResponse todoSaveResponse = todoService.registerTodos(todoSaveRequest, memberId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(todoSaveResponse)
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TodoDetailResponse>>> getTodos(
            @Valid @ModelAttribute TodoFilterRequest todoFilterRequest,
            @LoginId Long memberId
    ) {
        List<TodoDetailResponse> todos = todoService.toDetailResponses(todoFilterRequest, memberId);
        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(todos)
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoDetailResponse>> getTodoDetail(
            @PathVariable Long id,
            @LoginId Long memberId
    ) {
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
            @LoginId Long memberId
    ) {
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
            @LoginId Long memberId
    ) {
        todoService.deleteTodo(id, memberId, scope);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<TodoSaveResponse>> completeTodo(
            @PathVariable Long id,
            @LoginId Long memberId
    ) {
        TodoSaveResponse todoSaveResponse = todoService.completeTodo(id, memberId);
        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(todoSaveResponse)
                );
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<TodoDoneStats>> getStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @LoginId Long memberId
    ) {
        TodoDoneStats todoDoneStats = todoService.getTodoDoneStats(startDate, endDate, memberId);
        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(todoDoneStats)
                );
    }

    @GetMapping("/calendar")
    public ResponseEntity<ApiResponse<List<TodoCalendarView>>> getCalendarView(
            @RequestParam int year,
            @RequestParam int month,
            @LoginId Long memberId
    ) {
        List<TodoCalendarView> calendarView = todoService.getCalendarView(year, month, memberId);
        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(calendarView)
                );
    }

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<TodoDetailResponse>>> getTodayTodo(
            @LoginId Long memberId
    ) {
        List<TodoDetailResponse> todayTodos = todoService.getTodayTodo(memberId);
        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(todayTodos)
                );

    }
}
