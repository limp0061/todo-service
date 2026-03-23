package com.example.todoservice.todoTag.exception;

import com.example.todoservice.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TodoTagErrorCode implements ErrorCode {

    DUPLICATE_TODO_TAG(HttpStatus.CONFLICT, "중복된 일정 태그입니다");
    private final HttpStatus httpStatus;
    private final String message;

    TodoTagErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
