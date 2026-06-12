package org.example.dayleaf.domain.todo.dto;

import org.example.dayleaf.domain.todo.entity.Priority;

// 투두 수정 요청 DTO (전체 교체)
public record TodoUpdateRequest(
        String title,        // 투두 제목 (Node.title 수정)
        Priority priority,   // 우선순위
        Integer categoryId   // 카테고리 id (nullable, null이면 카테고리 해제)
) {
}
