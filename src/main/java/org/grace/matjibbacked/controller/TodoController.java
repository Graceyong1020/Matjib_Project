package org.grace.matjibbacked.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.grace.matjibbacked.dto.PageRequestDTO;
import org.grace.matjibbacked.dto.PageResponseDTO;
import org.grace.matjibbacked.dto.TodoDTO;
import org.grace.matjibbacked.service.TodoService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService service;

    @GetMapping("/{tno}")
    public TodoDTO get(@PathVariable("tno") Long tno) {
        return service.get(tno);
    }

    @GetMapping("/list")
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        return service.getList(pageRequestDTO);
    }
    @PostMapping("/")
    public Map<String, Long> register(@RequestBody TodoDTO dto){ // RequestBody로 전달된 JSON 데이터를 TodoDTO로 변환
        log.info("dto: " + dto);
        Long tno = service.register(dto);
        return Map.of("tno", tno);
    }

    //modify
    @PutMapping("/{tno}")
    public Map<String, String> modify(@PathVariable("tno") Long tno, @RequestBody TodoDTO todoDTO) {
        todoDTO.setTno(tno);

        service.modify(todoDTO);
        return Map.of("result", "success");
    }
    @DeleteMapping("/{tno}")
    public Map<String, String> remove(@PathVariable("tno") Long tno) {
        service.remove(tno);
        return Map.of("result", "success");
    }
}
