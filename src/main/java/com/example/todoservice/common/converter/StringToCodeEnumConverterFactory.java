package com.example.todoservice.common.converter;

import com.example.todoservice.common.exception.BusinessException;
import com.example.todoservice.common.exception.CommonErrorCode;
import com.example.todoservice.common.type.CodeEnum;
import java.util.Arrays;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class StringToCodeEnumConverterFactory implements ConverterFactory<String, CodeEnum> {

    @Override
    public <T extends CodeEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return code -> {
            if (!StringUtils.hasText(code)) {
                return null;
            }

            return Arrays.stream(targetType.getEnumConstants())
                    .filter(e -> e.getCode().equals(code))
                    .findAny()
                    .orElseThrow(() -> new BusinessException(CommonErrorCode.INVALID_INPUT));
        };
    }
}
