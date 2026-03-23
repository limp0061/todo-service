package com.example.todoservice.todo.service;

import com.example.todoservice.common.exception.BusinessException;
import com.example.todoservice.todo.domain.RepeatType;
import com.example.todoservice.todo.exception.TodoErrorCode;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class TodoValidator {

    public void validateTodoRequest(LocalDate startDate, LocalDate dueDate, LocalDate repeatEndDate,
                                    RepeatType repeatType) {

        if (dueDate != null && startDate.isAfter(dueDate)) {
            throw new BusinessException(TodoErrorCode.TODO_INVALID_DUE_DATE);
        }

        if (repeatType != RepeatType.NONE) {
            if (repeatEndDate == null) {
                throw new BusinessException(TodoErrorCode.TODO_REPEAT_END_DATE_REQUIRED);
            }
            if (startDate.isAfter(repeatEndDate)) {
                throw new BusinessException(TodoErrorCode.TODO_INVALID_REPEAT_END_DATE);
            }
        }

        LocalDate maxEndDate = switch (repeatType) {
            case DAILY -> startDate.plusMonths(3);
            case WEEKLY -> startDate.plusMonths(6);
            case MONTHLY -> startDate.plusYears(1);
            case YEARLY -> startDate.plusYears(3);
            case NONE -> startDate;
        };

        if (repeatEndDate.isAfter(maxEndDate)) {
            throw new BusinessException(TodoErrorCode.TODO_REPEAT_END_DATE_EXCEEDED);
        }
    }

    public void validateDateRange(LocalDate startDate, LocalDate dueDate) {
        if (dueDate != null && startDate.isAfter(dueDate)) {
            throw new BusinessException(TodoErrorCode.TODO_INVALID_DUE_DATE);
        }
    }
}
