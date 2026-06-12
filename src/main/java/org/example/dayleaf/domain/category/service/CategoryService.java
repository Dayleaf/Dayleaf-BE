package org.example.dayleaf.domain.category.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.dayleaf.domain.category.dto.CategoryCreateRequest;
import org.example.dayleaf.domain.category.dto.CategoryResponse;
import org.example.dayleaf.domain.category.dto.CategoryUpdateRequest;
import org.example.dayleaf.domain.category.entity.Category;
import org.example.dayleaf.domain.category.repository.CategoryRepository;
import org.example.dayleaf.domain.todo.repository.TodoRepository;
import org.example.dayleaf.global.exception.CustomException;
import org.example.dayleaf.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TodoRepository todoRepository;

    // 카테고리 생성
    @Transactional
    public CategoryResponse createCategory(Long memberId, CategoryCreateRequest request) {
        Category category = Category.builder()
                .categoryName(request.categoryName())
                .memberId(memberId)
                .build();

        Category saved = categoryRepository.save(category);
        return CategoryResponse.from(saved);
    }

    // 카테고리 수정 (전체 교체)
    @Transactional
    public CategoryResponse updateCategory(Long memberId, Integer categoryId, CategoryUpdateRequest request) {
        Category category = findCategoryWithAuth(memberId, categoryId);

        // Category 엔티티 수정 불가로 인해 JPQL 쿼리로 직접 업데이트
        categoryRepository.updateCategoryName(categoryId, request.categoryName());

        return new CategoryResponse(category.getId(), request.categoryName());
    }

    // 카테고리 삭제 (hard delete, 연결된 투두의 category_id는 null 처리)
    @Transactional
    public void deleteCategory(Long memberId, Integer categoryId) {
        findCategoryWithAuth(memberId, categoryId);

        // 해당 카테고리를 사용 중인 투두의 category_id를 null로 일괄 처리
        todoRepository.clearCategoryByCategoryId(categoryId);

        categoryRepository.deleteById(categoryId);
    }

    // 내 카테고리 목록 조회
    public List<CategoryResponse> getCategories(Long memberId) {
        return categoryRepository.findAllByMemberId(memberId)
                .stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    // 카테고리 상세 조회
    public CategoryResponse getCategory(Long memberId, Integer categoryId) {
        Category category = findCategoryWithAuth(memberId, categoryId);
        return CategoryResponse.from(category);
    }

    // 카테고리 조회 + 권한 검증 (내부 헬퍼)
    private Category findCategoryWithAuth(Long memberId, Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        if (!category.getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.CATEGORY_ACCESS_DENIED);
        }

        return category;
    }
}
