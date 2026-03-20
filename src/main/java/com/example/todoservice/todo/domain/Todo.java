package com.example.todoservice.todo.domain;

import com.example.todoservice.common.domain.BaseEntity;
import com.example.todoservice.member.domain.Member;
import com.example.todoservice.todo.dto.TodoUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "todos")
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    // 배정일
    private LocalDate scheduledDate;

    // 종료일
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RepeatType repeatType;

    // 반복 종료일
    private LocalDate repeatEndDate;

    // 원본 Todo (원본이면 null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Todo parent;

    // 원본 여부 (true = 원본, false = 반복 인스턴스)
    @Column(nullable = false)
    private boolean isTemplate;

    @Builder
    public Todo(
            Member member, String title, String description, LocalDate scheduledDate, LocalDate dueDate,
            Priority priority, TodoStatus status, RepeatType repeatType, LocalDate repeatEndDate, Todo parent,
            boolean isTemplate
    ) {
        this.member = member;
        this.title = title;
        this.description = description;
        this.scheduledDate = scheduledDate;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.repeatType = repeatType;
        this.repeatEndDate = repeatEndDate;
        this.parent = parent;
        this.isTemplate = isTemplate;
    }

    public void update(TodoUpdateRequest request) {
        this.title = request.title();
        this.description = request.description();
        this.scheduledDate = request.scheduledDate();
        this.dueDate = request.dueDate();
        this.priority = request.priority();
        this.repeatType = request.repeatType();
        this.repeatEndDate = request.repeatEndDate();
    }


    public void updateOnlyThis(TodoUpdateRequest request) {
        this.title = request.title();
        this.description = request.description();
        this.scheduledDate = request.scheduledDate();
        this.dueDate = request.dueDate();
        this.priority = request.priority();
    }

    public void detachFromParent() {
        this.parent = null;
        this.isTemplate = true;
        this.repeatType = RepeatType.NONE;
        this.repeatEndDate = null;
    }

    public void detachAsNewOrigin() {
        this.parent = null;
        this.isTemplate = true;
    }

    public void completeTodo() {
        this.status = TodoStatus.DONE;
    }
}
