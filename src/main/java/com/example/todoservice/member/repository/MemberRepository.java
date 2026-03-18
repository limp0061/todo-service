package com.example.todoservice.member.repository;

import com.example.todoservice.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByLoginId(String loginId);

    Optional<Member> findByLoginId(String loginId);
}
