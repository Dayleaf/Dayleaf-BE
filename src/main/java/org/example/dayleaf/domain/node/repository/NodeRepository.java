package org.example.dayleaf.domain.node.repository;

import org.example.dayleaf.domain.node.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NodeRepository extends JpaRepository<Node, Long> {

    // 같은 부모 하위에서 현재 최대 position 조회 (신규 노드 position 계산용)
    @Query("SELECT COALESCE(MAX(n.position), -1) FROM Node n WHERE n.parent.id = :parentId AND n.archived = false")
    int findMaxPositionByParentId(@Param("parentId") Long parentId);

    // 최상위 노드(parent 없음) 중 현재 최대 position 조회
    @Query("SELECT COALESCE(MAX(n.position), -1) FROM Node n WHERE n.parent IS NULL AND n.archived = false AND n.memberId = :memberId")
    int findMaxPositionByMemberIdAndParentIsNull(@Param("memberId") Long memberId);

    // 노드 path 업데이트 (노드 저장 후 자신의 id가 확정되면 path를 갱신)
    @Modifying
    @Query("UPDATE Node n SET n.path = :path WHERE n.id = :id")
    void updatePath(@Param("id") Long id, @Param("path") String path);

    // 노드 soft delete (is_archived = true)
    @Modifying
    @Query("UPDATE Node n SET n.archived = true WHERE n.id = :id")
    void archiveById(@Param("id") Long id);

    // 노드 title 업데이트
    @Modifying
    @Query("UPDATE Node n SET n.title = :title WHERE n.id = :id")
    void updateTitle(@Param("id") Long id, @Param("title") String title);
}
