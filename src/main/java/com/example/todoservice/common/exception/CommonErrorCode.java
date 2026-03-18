package com.example.todoservice.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonErrorCode implements ErrorCode {

    INVALID_INPUT(HttpStatus.BAD_REQUEST, "유효하지 않은 값 입니다."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다");
    private final HttpStatus httpStatus;
    private final String message;

    CommonErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
