package com.example.todoservice.tag.service;

import com.example.todoservice.common.exception.BusinessException;
import com.example.todoservice.member.domain.Member;
import com.example.todoservice.member.exception.MemberErrorCode;
import com.example.todoservice.member.repository.MemberRepository;
import com.example.todoservice.tag.domain.Tag;
import com.example.todoservice.tag.dto.TagResponse;
import com.example.todoservice.tag.dto.TagSaveRequest;
import com.example.todoservice.tag.dto.TagUpdateRequest;
import com.example.todoservice.tag.exception.TagErrorCode;
import com.example.todoservice.tag.repository.TagRepository;
import com.example.todoservice.todoTag.repository.TodoTagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;
    private final TagValidator tagValidator;
    private final TodoTagRepository todoTagRepository;

    @Transactional
    public TagResponse registerTag(TagSaveRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        tagValidator.validateDuplicateTag(request.name(), request.color(), member.getId());

        Tag tag = request.toEntity(member);

        tagRepository.save(tag);

        return TagResponse.from(tag);
    }

    public List<TagResponse> getAllTags(Long memberId) {

        List<Tag> tags = tagRepository.findByMemberId(memberId);
        return tags.stream()
                .map(TagResponse::from)
                .toList();
    }

    @Transactional
    public TagResponse updateTag(TagUpdateRequest request, Long tagId, Long memberId) {
        Tag tag = tagRepository.findByIdAndMemberId(tagId, memberId)
                .orElseThrow(() -> new BusinessException(TagErrorCode.TAG_NOT_FOUND));

        if (request.name() != null && request.color() != null) {
            tagValidator.validateDuplicateTag(request.name(), request.color(), memberId);
        }

        tag.update(request.name(), request.color());

        return TagResponse.from(tag);
    }

    @Transactional
    public void deleteTag(Long tagId, Long memberId) {
        Tag tag = tagRepository.findByIdAndMemberId(tagId, memberId)
                .orElseThrow(() -> new BusinessException(TagErrorCode.TAG_NOT_FOUND));

        todoTagRepository.deleteByTagId(tag.getId());
        tagRepository.delete(tag);
    }
}
