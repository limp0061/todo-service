package com.example.todoservice.todo.domain;

import com.example.todoservice.common.exception.BusinessException;
import com.example.todoservice.common.exception.CommonErrorCode;
import com.example.todoservice.common.type.CodeEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public enum TodoStatus implements CodeEnum {
    TODO("todo", "해야 할 일"),
    IN_PROGRESS("in_progress", "진행 중"),
    DONE("done", "완료");

    private final String code;
    private final String desc;

    TodoStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static final Map<String, TodoStatus> CODE_MAP = Stream.of(values())
            .collect(Collectors.toMap(TodoStatus::getCode, Function.identity()));

    @JsonCreator
    public static TodoStatus from(String code) {
        if (!StringUtils.hasText(code)) {
            return null;
        }

        return Optional.ofNullable(CODE_MAP.get(code))
                .orElseThrow(() -> new BusinessException(CommonErrorCode.INVALID_INPUT));
    }
}
