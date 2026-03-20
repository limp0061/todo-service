package com.example.todoservice.todo.service;

import com.example.todoservice.common.exception.BusinessException;
import com.example.todoservice.todo.domain.Todo;
import com.example.todoservice.todo.dto.RepeatScope;
import com.example.todoservice.todo.dto.TodoUpdateRequest;
import com.example.todoservice.todo.exception.TodoErrorCode;
import com.example.todoservice.todo.repository.TodoRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TodoFactory {

    private final TodoRepository todoRepository;

    public List<Todo> createRepeatInstance(Todo origin) {
        List<Todo> instances = new ArrayList<>();
        LocalDate startDate = origin.getScheduledDate();
        LocalDate endDate = origin.getRepeatEndDate();

        while (!startDate.isAfter(endDate)) {
            startDate = switch (origin.getRepeatType()) {
                case DAILY -> startDate.plusDays(1);
                case WEEKLY -> startDate.plusWeeks(1);
                case MONTHLY -> startDate.plusMonths(1);
                case YEARLY -> startDate.plusYears(1);
                case NONE -> endDate.plusDays(1);
            };

            if (!startDate.isAfter(endDate)) {
                instances.add(
                        Todo.builder()
                                .member(origin.getMember())
                                .title(origin.getTitle())
                                .description(origin.getDescription())
                                .scheduledDate(startDate)
                                .dueDate(origin.getDueDate())
                                .priority(origin.getPriority())
                                .status(origin.getStatus())
                                .repeatType(origin.getRepeatType())
                                .parent(origin)
                                .repeatEndDate(endDate)
                                .isTemplate(false)
                                .build()
                );
            }
        }
        return instances;
    }

    public void updateRepeatInstance(Todo origin, TodoUpdateRequest request) {

        Todo parent = origin.getParent();
        switch (request.scope()) {
            case ONLY_THIS -> {
                origin.detachFromParent();
                origin.updateOnlyThis(request);
            }
            case FROM_THIS -> {
                Long parentId = parent != null ? parent.getId() : origin.getId();
                todoRepository.deleteAllByAfterOrigin(parentId, origin.getScheduledDate());

                origin.detachAsNewOrigin();
                origin.updateOnlyThis(request);

                List<Todo> repeatInstance = createRepeatInstance(origin);
                todoRepository.saveAll(repeatInstance);
            }
            case ALL -> {
                Todo realOrigin = parent != null
                        ? todoRepository.findById(parent.getId())
                        .orElseThrow(() -> new BusinessException(TodoErrorCode.TODO_NOT_FOUND))
                        : origin;

                todoRepository.deleteAllByParentId(realOrigin.getId());
                realOrigin.update(request);
                todoRepository.saveAll(createRepeatInstance(realOrigin));
            }
        }
    }

    public void deleteRepeatInstance(Todo origin, RepeatScope scope) {
        Todo parent = origin.getParent();
        switch (scope) {
            case ONLY_THIS -> {
                if (origin.isTemplate()) {
                    todoRepository.deleteAllByParentId(origin.getId());
                }
            }
            case FROM_THIS -> {
                Long parentId = parent != null ? parent.getId() : origin.getId();
                todoRepository.deleteAllByAfterOrigin(parentId, origin.getScheduledDate());
            }
            case ALL -> {
                Todo realOrigin = parent != null
                        ? todoRepository.findById(parent.getId())
                        .orElseThrow(() -> new BusinessException(TodoErrorCode.TODO_NOT_FOUND))
                        : origin;
                todoRepository.deleteAllByParentId(realOrigin.getId());
                todoRepository.deleteById(realOrigin.getId());
                return;
            }
        }
        todoRepository.deleteById(origin.getId());
    }
}
