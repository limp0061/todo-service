package com.example.todoservice.todo.dto;

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
public enum RepeatScope implements CodeEnum {
    ONLY_THIS("only_this"),
    FROM_THIS("from_this"),
    ALL("all");

    private final String code;

    RepeatScope(String code) {
        this.code = code;
    }

    public static final Map<String, RepeatScope> CODE_MAP = Stream.of(values())
            .collect(Collectors.toMap(RepeatScope::getCode, Function.identity()));

    @JsonCreator
    public static RepeatScope from(String code) {
        if (!StringUtils.hasText(code)) {
            return null;
        }

        return Optional.ofNullable(CODE_MAP.get(code))
                .orElseThrow(() -> new BusinessException(CommonErrorCode.INVALID_INPUT));
    }
}
