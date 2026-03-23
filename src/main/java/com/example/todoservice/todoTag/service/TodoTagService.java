package com.example.todoservice.todoTag.service;

import com.example.todoservice.common.exception.BusinessException;
import com.example.todoservice.tag.domain.Tag;
import com.example.todoservice.tag.exception.TagErrorCode;
import com.example.todoservice.tag.repository.TagRepository;
import com.example.todoservice.todo.domain.Todo;
import com.example.todoservice.todo.exception.TodoErrorCode;
import com.example.todoservice.todo.repository.TodoRepository;
import com.example.todoservice.todoTag.domain.TodoTag;
import com.example.todoservice.todoTag.dto.TodoTagResponse;
import com.example.todoservice.todoTag.repository.TodoTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoTagService {

    private final TodoRepository todoRepository;
    private final TagRepository tagRepository;
    private final TodoTagValidator todoTagValidator;
    private final TodoTagRepository todoTagRepository;

    @Transactional
    public TodoTagResponse addTagToTodo(Long todoId, Long tagId, Long memberId) {
        Todo todo = todoRepository.findByIdAndMemberId(todoId, memberId)
                .orElseThrow(() -> new BusinessException(TodoErrorCode.TODO_NOT_FOUND));

        Tag tag = tagRepository.findByIdAndMemberId(tagId, memberId)
                .orElseThrow(() -> new BusinessException(TagErrorCode.TAG_NOT_FOUND));

        todoTagValidator.validateDuplicate(todo.getId(), tag.getId());

        TodoTag todoTag = TodoTag.builder()
                .todo(todo)
                .tag(tag)
                .build();

        todoTagRepository.save(todoTag);

        return TodoTagResponse.from(todoTag);
    }
}
