package com.example.todoservice.todo.repository;

import static com.example.todoservice.todo.domain.QTodo.todo;
import static com.example.todoservice.todoTag.domain.QTodoTag.todoTag;

import com.example.todoservice.todo.domain.Priority;
import com.example.todoservice.todo.domain.Todo;
import com.example.todoservice.todo.domain.TodoStatus;
import com.example.todoservice.todo.dto.TodoCalendarView;
import com.example.todoservice.todo.dto.TodoDoneStats;
import com.example.todoservice.todo.dto.TodoFilterRequest;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Todo> findAllByFilter(TodoFilterRequest request, Long memberId) {

        return queryFactory.selectFrom(todo)
                .where(
                        todo.member.id.eq(memberId),
                        priorityEq(request.getPriority()),
                        statusEq(request.getStatus()),
                        tagIdEq(request.getTagId()),
                        scheduledDateBetween(request.getStartDate(), request.getEndDate())
                )
                .fetch();
    }

    private BooleanExpression tagIdEq(Long tagId) {
        if (tagId == null) {
            return null;
        }
        return todo.id.in(
                JPAExpressions.select(todoTag.todo.id)
                        .from(todoTag)
                        .where(todoTag.tag.id.eq(tagId))
        );
    }

    @Override
    public void deleteAllByAfterOrigin(Long id, LocalDate scheduledDate) {
        queryFactory.delete(todo)
                .where(
                        todo.parent.id.eq(id),
                        scheduledDateAfter(scheduledDate)
                ).execute();
    }

    @Override
    public TodoDoneStats findDoneStats(LocalDate startDate, LocalDate endDate, Long memberId) {
        return queryFactory.select(
                        Projections.constructor(TodoDoneStats.class,
                                todo.count(),
                                todo.status.when(TodoStatus.DONE).then(1L).otherwise(0L).sum()
                        )
                )
                .where(
                        todo.member.id.eq(memberId),
                        scheduledDateBetween(startDate, endDate)
                )
                .from(todo)
                .fetchOne();
    }

    @Override
    public List<TodoCalendarView> findCalendarView(LocalDate startDate, LocalDate endDate, Long memberId) {
        return queryFactory.select(
                        Projections.constructor(TodoCalendarView.class,
                                todo.scheduledDate,
                                todo.count()
                        )
                ).from(todo)
                .where(
                        todo.member.id.eq(memberId),
                        scheduledDateBetween(startDate, endDate)
                )
                .groupBy(todo.scheduledDate)
                .fetch();
    }

    private BooleanExpression priorityEq(Priority priority) {
        return priority != null ? todo.priority.eq(priority) : null;
    }

    private BooleanExpression statusEq(TodoStatus status) {
        return status != null ? todo.status.eq(status) : null;
    }

    private BooleanExpression scheduledDateBetween(LocalDate startDate, LocalDate endDate) {
        return todo.scheduledDate.between(startDate, endDate);
    }

    private BooleanExpression scheduledDateAfter(LocalDate scheduledDate) {
        return todo.scheduledDate.after(scheduledDate);
    }
}
