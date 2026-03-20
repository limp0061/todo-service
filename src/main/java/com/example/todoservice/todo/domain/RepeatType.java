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
public enum RepeatType implements CodeEnum {
    NONE("none", "반복 없음"),
    DAILY("daily", "매일 반복"),
    WEEKLY("weekly", "매주 반복"),
    MONTHLY("monthly", "매달 반복"),
    YEARLY("yearly", "매년 반복");

    private final String code;
    private final String desc;

    RepeatType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static final Map<String, RepeatType> CODE_MAP = Stream.of(values())
            .collect(Collectors.toMap(RepeatType::getCode, Function.identity()));

    @JsonCreator
    public static RepeatType from(String code) {
        if (!StringUtils.hasText(code)) {
            return null;
        }

        return Optional.ofNullable(CODE_MAP.get(code))
                .orElseThrow(() -> new BusinessException(CommonErrorCode.INVALID_INPUT));
    }
}
