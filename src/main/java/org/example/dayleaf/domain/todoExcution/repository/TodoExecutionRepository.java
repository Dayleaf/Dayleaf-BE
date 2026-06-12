package org.example.dayleaf.domain.todoexecution.repository;

import java.time.LocalDate;
import java.util.List;
import org.example.dayleaf.domain.todoexecution.entity.TodoExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoExecutionRepository extends JpaRepository<TodoExecution, Long> {

    // 특정 노드 목록 중 해당 기간에 실행 기록이 있는 nodeId 목록 조회 (날짜 기반 투두 조회용)
    @Query("SELECT DISTINCT te.nodeId FROM TodoExecution te WHERE te.nodeId IN :nodeIds AND te.date BETWEEN :from AND :to")
    List<Long> findExecutedNodeIdsByNodeIdsAndDateBetween(
            @Param("nodeIds") List<Long> nodeIds,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    // 특정 노드 목록 중 해당 연도·월에 실행 기록이 있는 distinct nodeId 수 조회 (연별 수행률용)
    @Query("SELECT MONTH(te.date), COUNT(DISTINCT te.nodeId) FROM TodoExecution te WHERE te.nodeId IN :nodeIds AND YEAR(te.date) = :year GROUP BY MONTH(te.date)")
    List<Object[]> countCompletedByMonthForYear(@Param("nodeIds") List<Long> nodeIds, @Param("year") int year);
}
