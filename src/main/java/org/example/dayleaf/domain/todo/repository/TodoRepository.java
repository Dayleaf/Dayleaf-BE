package org.example.dayleaf.domain.todo.repository;

import java.util.List;
import org.example.dayleaf.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // 회원의 활성 투두 목록 조회 (archived 제외)
    @Query("SELECT t FROM Todo t JOIN FETCH t.node n WHERE n.memberId = :memberId AND n.archived = false")
    List<Todo> findAllByMemberIdAndNotArchived(@Param("memberId") Long memberId);

    // 특정 nodeId 목록에 해당하는 투두 조회 (날짜 기반 조회용)
    @Query("SELECT t FROM Todo t JOIN FETCH t.node n WHERE n.id IN :nodeIds AND n.archived = false")
    List<Todo> findAllByNodeIdIn(@Param("nodeIds") List<Long> nodeIds);

    // 카테고리 삭제 시 해당 카테고리를 사용 중인 투두의 category_id를 null로 일괄 처리
    @Modifying
    @Query(value = "UPDATE todo SET category_id = null WHERE category_id = :categoryId", nativeQuery = true)
    void clearCategoryByCategoryId(@Param("categoryId") Integer categoryId);
}
