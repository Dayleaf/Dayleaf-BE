package org.example.dayleaf.domain.category.dto;

import org.example.dayleaf.domain.category.entity.Category;

// 카테고리 응답 DTO
public record CategoryResponse(
        Integer categoryId,
        String categoryName
) {

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getCategoryName()
        );
    }
}
