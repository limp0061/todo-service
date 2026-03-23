package com.example.todoservice.tag.exception;

import com.example.todoservice.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TagErrorCode implements ErrorCode {

    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "태그를 찾을 수 없습니다"),
    DUPLICATE_TAG(HttpStatus.CONFLICT, "중복된 태그입니다"),
    ;
    private final HttpStatus httpStatus;
    private final String message;

    TagErrorCode(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
