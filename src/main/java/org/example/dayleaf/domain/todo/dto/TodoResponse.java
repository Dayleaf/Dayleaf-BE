package org.example.dayleaf.domain.todo.dto;

import org.example.dayleaf.domain.todo.entity.Priority;
import org.example.dayleaf.domain.todo.entity.Todo;
import org.example.dayleaf.domain.todo.entity.TodoStatus;

// 투두 응답 DTO
public record TodoResponse(
        Long todoId,
        String title,
        Priority priority,
        TodoStatus status,
        Integer categoryId
) {

    public static TodoResponse from(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getNode().getTitle(),
                todo.getPriority(),
                todo.getStatus(),
                todo.getCategory() != null ? todo.getCategory().getId() : null
        );
    }
}
