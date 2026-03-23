package com.example.todoservice.tag.service;

import com.example.todoservice.common.exception.BusinessException;
import com.example.todoservice.tag.exception.TagErrorCode;
import com.example.todoservice.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TagValidator {

    private final TagRepository tagRepository;

    public void validateDuplicateTag(String name, String color, Long memberId) {
        if (tagRepository.existsByMemberIdAndNameAndColor(memberId, name, color)) {
            throw new BusinessException(TagErrorCode.DUPLICATE_TAG);
        }
    }
}
