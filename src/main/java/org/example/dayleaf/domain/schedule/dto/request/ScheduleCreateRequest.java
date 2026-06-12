package org.example.dayleaf.domain.schedule.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleCreateRequest(
        Long nodeId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        boolean allDay
) {
}
