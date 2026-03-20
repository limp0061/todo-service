package com.example.todoservice.initializer;

import com.example.todoservice.member.domain.Member;
import com.example.todoservice.member.repository.MemberRepository;
import com.example.todoservice.todo.domain.Priority;
import com.example.todoservice.todo.domain.RepeatType;
import com.example.todoservice.todo.domain.Todo;
import com.example.todoservice.todo.domain.TodoStatus;
import com.example.todoservice.todo.repository.TodoRepository;
import com.example.todoservice.todo.service.TodoFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;
    private final TodoFactory todoFactory;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Member member1 = Member.builder()
                .loginId("test1")
                .name("테스터1")
                .password("1234")
                .build();

        Member member2 = Member.builder()
                .loginId("test2")
                .name("테스터2")
                .password("1234")
                .build();

        memberRepository.saveAll(List.of(member1, member2));

        // 일반 Todo
        Todo normalTodo = Todo.builder()
                .member(member1)
                .title("일반 할 일")
                .description("반복 없는 일반 할 일")
                .scheduledDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(3))
                .priority(Priority.HIGH)
                .status(TodoStatus.TODO)
                .repeatType(RepeatType.NONE)
                .parent(null)
                .isTemplate(true)
                .build();

        todoRepository.save(normalTodo);

        // 매일 반복 Todo
        Todo dailyTodo = Todo.builder()
                .member(member1)
                .title("매일 운동하기")
                .description("매일 반복 할 일")
                .scheduledDate(LocalDate.now())
                .dueDate(LocalDate.now().plusMonths(1))
                .priority(Priority.MEDIUM)
                .status(TodoStatus.TODO)
                .repeatType(RepeatType.DAILY)
                .repeatEndDate(LocalDate.now().plusMonths(1))
                .parent(null)
                .isTemplate(true)
                .build();

        todoRepository.save(dailyTodo);

        // 매일 반복 인스턴스 생성
        List<Todo> dailyInstances = todoFactory.createRepeatInstance(dailyTodo);
        todoRepository.saveAll(dailyInstances);

        // 매주 반복 Todo
        Todo weeklyTodo = Todo.builder()
                .member(member1)
                .title("매주 회의")
                .description("매주 반복 할 일")
                .scheduledDate(LocalDate.now())
                .dueDate(LocalDate.now().plusMonths(3))
                .priority(Priority.URGENT)
                .status(TodoStatus.TODO)
                .repeatType(RepeatType.WEEKLY)
                .repeatEndDate(LocalDate.now().plusMonths(3))
                .parent(null)
                .isTemplate(true)
                .build();

        todoRepository.save(weeklyTodo);

        List<Todo> weeklyInstances = todoFactory.createRepeatInstance(weeklyTodo);
        todoRepository.saveAll(weeklyInstances);

        Todo doneTodo = Todo.builder()
                .member(member1)
                .title("완료된 할 일")
                .description("이미 완료된 할 일")
                .scheduledDate(LocalDate.now().minusDays(1))
                .priority(Priority.LOW)
                .status(TodoStatus.DONE)
                .repeatType(RepeatType.NONE)
                .parent(null)
                .isTemplate(true)
                .build();

        todoRepository.save(doneTodo);

        Todo member2Todo = Todo.builder()
                .member(member2)
                .title("테스터2 할 일")
                .scheduledDate(LocalDate.now())
                .priority(Priority.MEDIUM)
                .status(TodoStatus.TODO)
                .repeatType(RepeatType.NONE)
                .parent(null)
                .isTemplate(true)
                .build();

        todoRepository.save(member2Todo);
    }
}
