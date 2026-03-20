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
public enum Priority implements CodeEnum {
    LOW("low", "낮음"),
    MEDIUM("medium", "보통"),
    HIGH("high", "높음"),
    URGENT("urgent", "긴급");

    private final String code;
    private final String desc;

    Priority(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<String, Priority> CODE_MAP = Stream.of(values())
            .collect(Collectors.toMap(Priority::getCode, Function.identity()));

    @JsonCreator
    public static Priority from(String code) {
        if (!StringUtils.hasText(code)) {
            return null;
        }

        return Optional.ofNullable(CODE_MAP.get(code))
                .orElseThrow(() -> new BusinessException(CommonErrorCode.INVALID_INPUT));
    }
}