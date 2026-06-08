package org.example.dayleaf.domain.schedule.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import org.example.dayleaf.domain.schedule.entity.Schedule;

public record ScheduleResponse(
        Long nodeId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        boolean allDay
) {

    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getDate(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.isAllDay()
        );
    }
}
