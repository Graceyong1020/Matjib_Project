package org.grace.matjibbacked.repository.search;

import org.grace.matjibbacked.domain.Todo;
import org.grace.matjibbacked.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface TodoSearch {

    Page<Todo> search1(PageRequestDTO pageRequestDTO);
}
