package com.example.todoservice.todo.repository;

import static com.example.todoservice.todo.domain.QTodo.todo;

import com.example.todoservice.todo.domain.Priority;
import com.example.todoservice.todo.domain.Todo;
import com.example.todoservice.todo.domain.TodoStatus;
import com.example.todoservice.todo.dto.TodoFilterRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
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
                        scheduledDateBetween(request.getStartDate(), request.getEndDate())
                )
                .fetch();
    }

    @Override
    public void deleteAllByAfterOrigin(Long id, LocalDate scheduledDate) {
        queryFactory.delete(todo)
                .where(
                        todo.parent.id.eq(id),
                        scheduledDateAfter(scheduledDate)
                ).execute();
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
