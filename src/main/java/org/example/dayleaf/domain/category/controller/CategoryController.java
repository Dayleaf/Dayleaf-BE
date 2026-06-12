package org.example.dayleaf.domain.category.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dayleaf.domain.category.dto.CategoryCreateRequest;
import org.example.dayleaf.domain.category.dto.CategoryResponse;
import org.example.dayleaf.domain.category.dto.CategoryUpdateRequest;
import org.example.dayleaf.domain.category.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 생성
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestHeader("memberId") Long memberId,
            @RequestBody CategoryCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(memberId, request));
    }

    // 카테고리 수정 (전체 교체)
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @RequestHeader("memberId") Long memberId,
            @PathVariable Integer categoryId,
            @RequestBody CategoryUpdateRequest request
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(memberId, categoryId, request));
    }

    // 카테고리 삭제
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @RequestHeader("memberId") Long memberId,
            @PathVariable Integer categoryId
    ) {
        categoryService.deleteCategory(memberId, categoryId);
        return ResponseEntity.noContent().build();
    }

    // 내 카테고리 목록 조회
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories(
            @RequestHeader("memberId") Long memberId
    ) {
        return ResponseEntity.ok(categoryService.getCategories(memberId));
    }

    // 카테고리 상세 조회
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategory(
            @RequestHeader("memberId") Long memberId,
            @PathVariable Integer categoryId
    ) {
        return ResponseEntity.ok(categoryService.getCategory(memberId, categoryId));
    }
}
