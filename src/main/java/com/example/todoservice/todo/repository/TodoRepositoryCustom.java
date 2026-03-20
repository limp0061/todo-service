package com.example.todoservice.todo.repository;

import com.example.todoservice.todo.domain.Todo;
import com.example.todoservice.todo.dto.TodoFilterRequest;
import java.time.LocalDate;
import java.util.List;

public interface TodoRepositoryCustom {
    List<Todo> findAllByFilter(TodoFilterRequest request, Long memberId);

    void deleteAllByAfterOrigin(Long id, LocalDate scheduledDate);
}
