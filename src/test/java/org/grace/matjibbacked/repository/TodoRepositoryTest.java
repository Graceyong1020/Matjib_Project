package org.grace.matjibbacked.repository;

import lombok.extern.log4j.Log4j2;
import org.grace.matjibbacked.domain.Todo;
import org.grace.matjibbacked.dto.TodoDTO;
import org.grace.matjibbacked.service.TodoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Log4j2
@SpringBootTest
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    @Qualifier("todoServiceImpl")
    private TodoService todoService;

    @Test
    public void test1() {
        Assertions.assertNotNull(todoRepository);
        log.info("----------------------");
        log.info(todoRepository.getClass().getName());
    }

    @Test
    public void testInser() {
        for (int i = 1; i <= 100; i++) {
            Todo todo = Todo.builder()
                    .title("Sample..." + i)
                    .dueDate(LocalDate.now())
                    .writer("user" + i)
                    .build();
            todoRepository.save(todo);
        }
    }

    @Test
    public void testRead() {
        Long tno = 1L;
        Optional<Todo> result = todoRepository.findById(tno);
        Todo todo = result.orElseThrow();
        log.info(todo);
    }

    @Test
    public void testUpdate() {
        Long tno = 1L;
        Optional<Todo> result = todoRepository.findById(tno);
        Todo todo = result.orElseThrow();
        todo.changeTitle("modify....");
        todo.changeComplete(true);
        todo.changeDueDate(LocalDate.now().plusDays(2));
        todoRepository.save(todo);
    }

    @Test
    public void testPaging() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("tno").descending());
        Page<Todo> result = todoRepository.findAll(pageable);
        log.info(result.getTotalElements());
        result.getContent().forEach(todo -> log.info(todo));
    }

 /*   @Test
    public void testSearch1() {
        todoRepository.search1();
    }*/

    @Test
    @Transactional
    public void testGet() {
        // Ensure the Todo entity is saved before testing
        Todo todo = Todo.builder()
                .title("Test Todo")
                .writer("Test Writer")
                .dueDate(LocalDate.now())
                .complete(false)
                .build();
        todoRepository.save(todo);

        Long tno = todo.getTno();
        TodoDTO todoDTO = todoService.get(tno);
        log.info(todoDTO);
    }
}