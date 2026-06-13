package org.example.dayleaf.domain.repeat.dto.request;

import java.time.LocalDate;
import org.example.dayleaf.domain.repeat.entity.RepeatStatus;
import org.example.dayleaf.domain.repeat.entity.RepeatType;

public record RepeatCreateRequest(
        Long nodeId,
        LocalDate startDate,
        LocalDate endDate,
        boolean active,
        RepeatType repeatType,
        Integer repeatInterval,
        String daysOfWeek,
        Integer dayOfMonth,
        Integer monthOfYear,
        RepeatStatus status
) {
}
