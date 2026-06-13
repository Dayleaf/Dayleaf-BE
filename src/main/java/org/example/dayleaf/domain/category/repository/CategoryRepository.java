package org.example.dayleaf.domain.category.repository;

import java.util.List;
import org.example.dayleaf.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // 회원의 카테고리 목록 조회
    List<Category> findAllByMemberId(Long memberId);

    // 카테고리명 업데이트 (Category 엔티티 수정 불가로 인해 JPQL 사용)
    @Modifying
    @Query("UPDATE Category c SET c.categoryName = :name WHERE c.id = :id")
    void updateCategoryName(@Param("id") Integer id, @Param("name") String name);
}
