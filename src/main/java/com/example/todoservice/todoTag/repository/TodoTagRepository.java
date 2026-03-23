package com.example.todoservice.todoTag.repository;

import com.example.todoservice.tag.domain.Tag;
import com.example.todoservice.todoTag.domain.TodoTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoTagRepository extends JpaRepository<TodoTag, Long> {
    boolean existsByTodoIdAndTagId(Long todoId, Long tagId);

    void deleteByTagId(Long tagId);

    void deleteByTodoId(Long todoId);

    @Query("SELECT tt.tag FROM TodoTag tt WHERE tt.todo.id = :todoId")
    List<Tag> findTagsByTodoId(@Param("todoId") Long todoId);

    @Query("SELECT tt FROM TodoTag tt WHERE tt.todo.id in :todoIds")
    List<TodoTag> findTagsByTodoIdIn(@Param("todoIds") List<Long> todoIds);

}
