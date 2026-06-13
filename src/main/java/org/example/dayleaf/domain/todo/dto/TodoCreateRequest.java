package org.example.dayleaf.domain.todo.dto;

import org.example.dayleaf.domain.todo.entity.Priority;

// 투두 생성 요청 DTO (Node 동시 생성)
public record TodoCreateRequest(
        Long parentId,       // 부모 node_id (null이면 최상위)
        String title,        // 투두 제목 (Node.title)
        Priority priority,   // 우선순위
        Integer categoryId   // 카테고리 id (nullable)
) {
}
