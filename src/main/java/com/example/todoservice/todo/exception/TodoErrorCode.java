package com.example.todoservice.todo.exception;

import com.example.todoservice.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TodoErrorCode implements ErrorCode {

    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다"),
    TODO_INVALID_DUE_DATE(HttpStatus.BAD_REQUEST, "종료일은 시작일 이후여햐 합니다"),
    TODO_INVALID_REPEAT_END_DATE(HttpStatus.BAD_REQUEST, "반복 종료일은 시작일 이후여햐 합니다"),
    TODO_REPEAT_END_DATE_REQUIRED(HttpStatus.BAD_REQUEST, "반복 종료일은 필수 입니다"),
    TODO_CANNOT_SET_REPEAT(HttpStatus.BAD_REQUEST, "이 일정만 수정 시 반복 설정을 할 수 없습니다"),
    TODO_CANNOT_CHANGE_REPEAT(HttpStatus.BAD_REQUEST, "항후 일정 수정 시 반복 설정을 변경 할 수 없습니다"),
    TODO_REPEAT_END_DATE_EXCEEDED(HttpStatus.BAD_REQUEST, "반복 종료일이 최대 반복 기간을 초과했습니다 (매일:3개월 / 매주:6개월 / 매달:1년 / 매년:3년)");


    private final HttpStatus httpStatus;
    private final String message;

    TodoErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
