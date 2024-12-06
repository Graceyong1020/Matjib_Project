package org.grace.matjibbacked.service;


import lombok.extern.log4j.Log4j2;
import org.grace.matjibbacked.dto.PageRequestDTO;
import org.grace.matjibbacked.dto.TodoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@Log4j2
@SpringBootTest
public class TodoServiceTest {

    @Autowired
    TodoService todoService;

    @Test
   public void testRegister() {
        TodoDTO todoDTO = TodoDTO.builder()
                .title("Sample")
                .writer("user")
                .complete(false)
                .dueDate(LocalDate.now())
                .build();

        log.info(todoService.register(todoDTO));
    }

    @Test
    public void testGetList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();
        log.info(todoService.getList(pageRequestDTO));
    }
}
