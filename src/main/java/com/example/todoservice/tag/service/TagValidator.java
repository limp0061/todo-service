package com.example.todoservice.tag.service;

import com.example.todoservice.common.exception.BusinessException;
import com.example.todoservice.tag.dto.TagSaveRequest;
import com.example.todoservice.tag.exception.TagErrorCode;
import com.example.todoservice.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TagValidator {

    private final TagRepository tagRepository;

    public void validateDuplicateTag(TagSaveRequest request, Long memberId) {
        if (tagRepository.existsByMemberIdAndNameAndColor(memberId, request.name(), request.color())) {
            throw new BusinessException(TagErrorCode.DUPLICATE_TAG);
        }
    }
}
