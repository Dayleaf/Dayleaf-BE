package org.example.dayleaf.domain.schedule.service;

import lombok.RequiredArgsConstructor;
import org.example.dayleaf.domain.node.entity.Node;
import org.example.dayleaf.domain.node.repository.NodeRepository;
import org.example.dayleaf.domain.schedule.dto.request.ScheduleCreateRequest;
import org.example.dayleaf.domain.schedule.dto.request.ScheduleUpdateRequest;
import org.example.dayleaf.domain.schedule.dto.response.ScheduleResponse;
import org.example.dayleaf.domain.schedule.entity.Schedule;
import org.example.dayleaf.domain.schedule.repository.ScheduleRepository;
import org.example.dayleaf.global.exception.CustomException;
import org.example.dayleaf.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final NodeRepository nodeRepository;

    @Transactional
    public ScheduleResponse createSchedule(ScheduleCreateRequest request) {
        Node node = nodeRepository.findById(request.nodeId())
                .orElseThrow(() -> new CustomException(ErrorCode.NODE_NOT_FOUND));

        if (scheduleRepository.existsById(request.nodeId())) {
            throw new CustomException(ErrorCode.SCHEDULE_ALREADY_EXISTS);
        }

        Schedule schedule = Schedule.builder()
                .node(node)
                .date(request.date())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .allDay(request.allDay())
                .build();

        Schedule savedSchedule = scheduleRepository.save(schedule);

        return ScheduleResponse.from(savedSchedule);
    }

    @Transactional
    public ScheduleResponse updateSchedule(Long nodeId, ScheduleUpdateRequest request) {
        Schedule schedule = scheduleRepository.findById(nodeId)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        schedule.update(
                request.date(),
                request.startTime(),
                request.endTime(),
                request.allDay()
        );

        return ScheduleResponse.from(schedule);
    }
}
