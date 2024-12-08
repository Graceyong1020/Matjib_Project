package org.grace.matjibbacked.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.grace.matjibbacked.domain.Todo;
import org.grace.matjibbacked.dto.PageRequestDTO;
import org.grace.matjibbacked.dto.PageResponseDTO;
import org.grace.matjibbacked.dto.TodoDTO;
import org.grace.matjibbacked.repository.TodoRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository; // 의존성 주입

    @Override
    public TodoDTO get(Long tno) {
        Optional<Todo> result = todoRepository.findById(tno);
        Todo todo = result.orElseThrow(() -> new NoSuchElementException("Todo not found with tno: " + tno));
        return entityToDTO(todo);
    }

    @Override
    public Long register(TodoDTO dto) {

        Todo todo = dtoToEntity(dto);
        log.info("Saving Todo with dueDate: " + todo.getDueDate()); // 날짜 형식 확인
        Todo result = todoRepository.save(todo);
        return result.getTno();
    }

    @Override
    public void modify(TodoDTO dto) { //기존 엔티티 가져와서 수정
        Optional<Todo> result = todoRepository.findById(dto.getTno());

        Todo todo = result.orElseThrow();

        todo.changeTitle(dto.getTitle());
        todo.changeWriter(dto.getWriter());
        todo.changeDueDate(dto.getDueDate());
        todo.changeComplete(dto.isComplete());

        todoRepository.save(todo);


    }

    @Override
    public void remove(Long tno) {

        todoRepository.deleteById(tno);

    }

    @Override
    public PageResponseDTO<TodoDTO> getList(PageRequestDTO pageRequestDTO) {

        // JPA
        Page<Todo> result = todoRepository.search1(pageRequestDTO);

        // Todo List => TodoDTO List
        List<TodoDTO> dtoList = result
                .get()
                .map(todo -> entityToDTO(todo))
                .collect(Collectors.toList());

        PageResponseDTO<TodoDTO> responseDTO =
                PageResponseDTO.<TodoDTO>withAll()
                        .dtoList(dtoList)
                        .pageRequestDTO(pageRequestDTO)
                        .totalCount(result.getTotalElements())
                        .build();


        return responseDTO;
    }

}
