package org.grace.matjibbacked.repository.search;

import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.grace.matjibbacked.domain.QTodo;
import org.grace.matjibbacked.domain.Todo;
import org.grace.matjibbacked.dto.PageRequestDTO;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
public class TodoSearchImpl extends QuerydslRepositorySupport implements TodoSearch {

    public TodoSearchImpl() {
        super(Todo.class);
    }

    @Override
    public Page<Todo> search1(PageRequestDTO pageRequestDTO) {
        log.info("search1............");
        QTodo todo = QTodo.todo;
        JPQLQuery<Todo> query = from(todo);

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage()-1,
                pageRequestDTO.getSize(),
                Sort.by("tno").descending());

        this.getQuerydsl().applyPagination(pageable, query);
       List<Todo> list =  query.fetch(); // list data
        long total = query.fetchCount(); // total count
        return new PageImpl<>(list, pageable, total);
    }
}
