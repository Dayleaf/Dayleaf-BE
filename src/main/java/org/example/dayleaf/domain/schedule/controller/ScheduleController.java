package org.example.dayleaf.domain.schedule.controller;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dayleaf.domain.schedule.dto.request.ScheduleCreateRequest;
import org.example.dayleaf.domain.schedule.dto.request.ScheduleUpdateRequest;
import org.example.dayleaf.domain.schedule.dto.response.ScheduleResponse;
import org.example.dayleaf.domain.schedule.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(
            @RequestBody ScheduleCreateRequest request
    ) {
        ScheduleResponse response = scheduleService.createSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{nodeId}")
    public ResponseEntity<ScheduleResponse> updateSchedule(
            @PathVariable Long nodeId,
            @RequestBody ScheduleUpdateRequest request
    ) {
        ScheduleResponse response = scheduleService.updateSchedule(nodeId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{nodeId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long nodeId
    ) {
        scheduleService.deleteSchedule(nodeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getSchedules() {
        List<ScheduleResponse> response = scheduleService.getSchedules();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/month")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByMonth(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        List<ScheduleResponse> response = scheduleService.getSchedulesByMonth(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/week")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByWeek(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        List<ScheduleResponse> response = scheduleService.getSchedulesByWeek(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/day")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByDay(
            @RequestParam LocalDate date
    ) {
        List<ScheduleResponse> response = scheduleService.getSchedulesByDay(date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> getSchedule(
            @PathVariable Long scheduleId
    ) {
        ScheduleResponse response = scheduleService.getSchedule(scheduleId);
        return ResponseEntity.ok(response);
    }
}
