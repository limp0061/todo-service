package com.example.todoservice.todoTag.repository;

import com.example.todoservice.todoTag.domain.TodoTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoTagRepository extends JpaRepository<TodoTag, Long> {
    boolean existsByTodoIdAndTagId(Long todoId, Long tagId);

}
