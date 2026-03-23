package com.example.todoservice.todo.manager;

import com.example.todoservice.common.redis.RedisConstants;
import com.example.todoservice.common.redis.RedisManager;
import com.example.todoservice.todo.dto.TodoDetailResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TodoCacheManager {

    private final RedisManager redisManager;

    public List<TodoDetailResponse> getTodayTodoCache(Long memberId) {
        return redisManager.get(RedisConstants.TODO + memberId);
    }

    public void setTodayTodoCache(Long memberId, List<TodoDetailResponse> cachedTodos) {
        redisManager.set(RedisConstants.TODO + memberId, cachedTodos, getSecondsUntilMidnight(), TimeUnit.SECONDS);
    }

    public void evictTodayTodoCache(Long memberId) {
        redisManager.delete(RedisConstants.TODO + memberId);
    }

    private long getSecondsUntilMidnight() {
        LocalDateTime midnight = LocalDate.now().plusDays(1).atStartOfDay();
        return LocalDateTime.now().until(midnight, ChronoUnit.SECONDS);
    }
}
