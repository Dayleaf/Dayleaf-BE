package org.example.dayleaf.domain.todo.dto;

import java.util.List;

// 연별 투두 수행률 응답 DTO
public record YearlyStatsResponse(
        int year,
        List<MonthStats> months
) {

    // 월별 수행률 통계
    public record MonthStats(
            int month,           // 월 (1~12)
            int totalCount,      // 전체 투두 수
            int completedCount,  // 완료된 투두 수
            double completionRate // 수행률 (%)
    ) {
    }
}
