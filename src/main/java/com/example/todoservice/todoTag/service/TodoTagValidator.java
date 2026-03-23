package com.example.todoservice.todoTag.service;

import com.example.todoservice.common.exception.BusinessException;
import com.example.todoservice.todoTag.exception.TodoTagErrorCode;
import com.example.todoservice.todoTag.repository.TodoTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TodoTagValidator {

    private final TodoTagRepository todoTagRepository;

    public void validateDuplicate(Long todoId, Long tagId) {
        if (todoTagRepository.existsByTodoIdAndTagId(todoId, tagId)) {
            throw new BusinessException(TodoTagErrorCode.DUPLICATE_TODO_TAG);
        }
    }
}
