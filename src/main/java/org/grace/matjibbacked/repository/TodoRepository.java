package org.grace.matjibbacked.repository;

import org.grace.matjibbacked.domain.Todo;
import org.grace.matjibbacked.repository.search.TodoSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoSearch {
}
