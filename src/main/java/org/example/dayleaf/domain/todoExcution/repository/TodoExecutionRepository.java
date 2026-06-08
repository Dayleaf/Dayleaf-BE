package org.example.dayleaf.domain.todoexecution.repository;

import org.example.dayleaf.domain.todoexecution.entity.TodoExecution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoExecutionRepository extends JpaRepository<TodoExecution, Long> {
}
