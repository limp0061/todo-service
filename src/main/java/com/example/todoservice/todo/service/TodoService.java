package com.example.todoservice.todo.service;

import com.example.todoservice.common.exception.BusinessException;
import com.example.todoservice.member.domain.Member;
import com.example.todoservice.member.exception.MemberErrorCode;
import com.example.todoservice.member.repository.MemberRepository;
import com.example.todoservice.todo.domain.RepeatType;
import com.example.todoservice.todo.domain.Todo;
import com.example.todoservice.todo.dto.RepeatScope;
import com.example.todoservice.todo.dto.TodoFilterRequest;
import com.example.todoservice.todo.dto.TodoSaveRequest;
import com.example.todoservice.todo.dto.TodoSaveResponse;
import com.example.todoservice.todo.dto.TodoUpdateRequest;
import com.example.todoservice.todo.exception.TodoErrorCode;
import com.example.todoservice.todo.repository.TodoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;
    private final TodoValidator todoValidator;
    private final TodoFactory todoFactory;

    @Transactional
    public TodoSaveResponse registerTodos(TodoSaveRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        todoValidator.validateTodoRequest(
                request.scheduledDate(),
                request.dueDate(),
                request.repeatEndDate(),
                request.repeatType()
        );

        Todo origin = request.toEntity(member);
        todoRepository.save(origin);

        if (request.repeatType() != RepeatType.NONE) {
            List<Todo> instances = todoFactory.createRepeatInstance(origin);
            todoRepository.saveAll(instances);
        }
        return TodoSaveResponse.from(origin);
    }

    public List<TodoSaveResponse> getTodos(TodoFilterRequest request, Long memberId) {
        todoValidator.validateDateRange(
                request.getStartDate(),
                request.getEndDate()
        );

        return todoRepository.findAllByFilter(request, memberId)
                .stream()
                .map(TodoSaveResponse::from)
                .toList();
    }

    public TodoSaveResponse getTodoDetail(Long todoId, Long memberId) {
        Todo todo = todoRepository.findByIdAndMemberId(todoId, memberId)
                .orElseThrow(() -> new BusinessException(TodoErrorCode.TODO_NOT_FOUND));

        return TodoSaveResponse.from(todo);
    }

    @Transactional
    public TodoSaveResponse updateTodo(TodoUpdateRequest request, Long todoId, Long memberId) {
        Todo todo = todoRepository.findByIdAndMemberId(todoId, memberId)
                .orElseThrow(() -> new BusinessException(TodoErrorCode.TODO_NOT_FOUND));

        todoValidator.validateTodoRequest(
                request.scheduledDate(),
                request.dueDate(),
                request.repeatEndDate(),
                request.repeatType()
        );

        if (todo.getRepeatType() == RepeatType.NONE) {
            todo.update(request);
        } else {
            todoFactory.updateRepeatInstance(todo, request);
        }

        return TodoSaveResponse.from(todo);
    }

    @Transactional
    public void deleteTodo(Long todoId, Long memberId, RepeatScope scope) {
        Todo todo = todoRepository.findByIdAndMemberId(todoId, memberId)
                .orElseThrow(() -> new BusinessException(TodoErrorCode.TODO_NOT_FOUND));

        if (todo.getRepeatType() == RepeatType.NONE) {
            todoRepository.delete(todo);
        } else {
            todoFactory.deleteRepeatInstance(todo, scope);
        }
    }

    @Transactional
    public TodoSaveResponse completeTodo(Long todoId, Long memberId) {
        Todo todo = todoRepository.findByIdAndMemberId(todoId, memberId)
                .orElseThrow(() -> new BusinessException(TodoErrorCode.TODO_NOT_FOUND));

        todo.completeTodo();

        return TodoSaveResponse.from(todo);
    }
}
