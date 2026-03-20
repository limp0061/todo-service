package com.example.todoservice.todo.repository;

import com.example.todoservice.todo.domain.Todo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom {

    Optional<Todo> findByIdAndMemberId(Long todoId, Long memberId);

    void deleteAllByParentId(Long id);
}
