package org.grace.matjibbacked.service;

import org.grace.matjibbacked.domain.Todo;
import org.grace.matjibbacked.dto.PageRequestDTO;
import org.grace.matjibbacked.dto.PageResponseDTO;
import org.grace.matjibbacked.dto.TodoDTO;

public interface TodoService {

    TodoDTO get(Long tno);

    Long register(TodoDTO dto);

    void modify(TodoDTO dto);
    void remove(Long tno);

    PageResponseDTO<TodoDTO> getList(PageRequestDTO pageRequestDTO);

    default TodoDTO entityToDTO(Todo todo) {

        return TodoDTO.builder()
                .tno(todo.getTno())
                .title(todo.getTitle())
                .complete(todo.isComplete())
                .writer(todo.getWriter())
                .dueDate(todo.getDueDate())
                .build();

    }

    default Todo dtoToEntity(TodoDTO todoDTO) {

        return Todo.builder()
                .tno(todoDTO.getTno())
                .title(todoDTO.getTitle())
                .complete(todoDTO.isComplete())
                .writer(todoDTO.getWriter())
                .dueDate(todoDTO.getDueDate())
                .build();

    }
}
