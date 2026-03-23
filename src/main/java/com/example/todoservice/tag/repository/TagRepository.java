package com.example.todoservice.tag.repository;

import com.example.todoservice.tag.domain.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByMemberIdAndNameAndColor(Long memberId, String name, String color);

    List<Tag> findByMemberId(Long memberId);

    Optional<Tag> findByIdAndMemberId(Long aLong, Long memberId);
}
