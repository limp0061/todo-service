package com.example.todoservice.todo.service;

import com.example.todoservice.common.exception.BusinessException;
import com.example.todoservice.member.domain.Member;
import com.example.todoservice.member.exception.MemberErrorCode;
import com.example.todoservice.member.repository.MemberRepository;
import com.example.todoservice.tag.dto.TagResponse;
import com.example.todoservice.todo.domain.RepeatType;
import com.example.todoservice.todo.domain.Todo;
import com.example.todoservice.todo.dto.RepeatScope;
import com.example.todoservice.todo.dto.TodoCalendarView;
import com.example.todoservice.todo.dto.TodoDetailResponse;
import com.example.todoservice.todo.dto.TodoDoneStats;
import com.example.todoservice.todo.dto.TodoFilterRequest;
import com.example.todoservice.todo.dto.TodoSaveRequest;
import com.example.todoservice.todo.dto.TodoSaveResponse;
import com.example.todoservice.todo.dto.TodoUpdateRequest;
import com.example.todoservice.todo.exception.TodoErrorCode;
import com.example.todoservice.todo.repository.TodoRepository;
import com.example.todoservice.todoTag.domain.TodoTag;
import com.example.todoservice.todoTag.repository.TodoTagRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    private final TodoTagRepository todoTagRepository;

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

    public List<TodoDetailResponse> getTodos(TodoFilterRequest request, Long memberId) {
        todoValidator.validateDateRange(
                request.getStartDate(),
                request.getEndDate()
        );

        List<Todo> todos = todoRepository.findAllByFilter(request, memberId);
        List<Long> todoIds = todos.stream()
                .map(Todo::getId)
                .toList();

        List<TodoTag> todoTags = todoTagRepository.findTagsByTodoIdIn(todoIds);

        Map<Long, List<TagResponse>> toMap = todoTags.stream()
                .collect(Collectors.groupingBy(
                        todoTag -> todoTag.getTodo().getId(),
                        Collectors.mapping(todoTag -> TagResponse.from(todoTag.getTag()), Collectors.toList())
                ));

        return todos.stream()
                .map(
                        todo -> TodoDetailResponse.of(
                                todo,
                                toMap.getOrDefault(todo.getId(), List.of())
                        )
                ).toList();
    }

    public TodoDetailResponse getTodoDetail(Long todoId, Long memberId) {
        Todo todo = todoRepository.findByIdAndMemberId(todoId, memberId)
                .orElseThrow(() -> new BusinessException(TodoErrorCode.TODO_NOT_FOUND));

        List<TagResponse> tags = todoTagRepository.findTagsByTodoId(todoId)
                .stream()
                .map(TagResponse::from)
                .toList();

        return TodoDetailResponse.of(todo, tags);
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

        todoTagRepository.deleteByTodoId(todoId);
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

    public TodoDoneStats getTodoDoneStats(LocalDate startDate, LocalDate endDate, Long memberId) {

        return todoRepository.findDoneStats(startDate, endDate, memberId);

    }

    public List<TodoCalendarView> getCalendarView(int year, int month, Long memberId) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        return todoRepository.findCalendarView(startDate, endDate, memberId);
    }
}
