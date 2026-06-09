package org.example.dayleaf.domain.schedule.controller;

import lombok.RequiredArgsConstructor;
import org.example.dayleaf.domain.schedule.dto.request.ScheduleCreateRequest;
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
}
