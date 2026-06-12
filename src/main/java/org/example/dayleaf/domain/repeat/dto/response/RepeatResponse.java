package org.example.dayleaf.domain.repeat.dto.response;

import java.time.LocalDate;
import org.example.dayleaf.domain.repeat.entity.Repeat;
import org.example.dayleaf.domain.repeat.entity.RepeatStatus;
import org.example.dayleaf.domain.repeat.entity.RepeatType;

public record RepeatResponse(
        Integer routineId,
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

    public static RepeatResponse from(Repeat repeat) {
        return new RepeatResponse(
                repeat.getRoutineId(),
                repeat.getNode().getId(),
                repeat.getStartDate(),
                repeat.getEndDate(),
                repeat.isActive(),
                repeat.getRepeatType(),
                repeat.getRepeatInterval(),
                repeat.getDaysOfWeek(),
                repeat.getDayOfMonth(),
                repeat.getMonthOfYear(),
                repeat.getStatus()
        );
    }
}
