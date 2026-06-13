package org.example.dayleaf.domain.repeat.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dayleaf.domain.node.entity.Node;
import org.example.dayleaf.domain.node.repository.NodeRepository;
import org.example.dayleaf.domain.repeat.dto.request.RepeatCreateRequest;
import org.example.dayleaf.domain.repeat.dto.request.RepeatUpdateRequest;
import org.example.dayleaf.domain.repeat.dto.response.RepeatResponse;
import org.example.dayleaf.domain.repeat.entity.Repeat;
import org.example.dayleaf.domain.repeat.repository.RepeatRepository;
import org.example.dayleaf.global.exception.CustomException;
import org.example.dayleaf.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RepeatService {

    private final RepeatRepository repeatRepository;
    private final NodeRepository nodeRepository;

    @Transactional
    public RepeatResponse createRepeat(RepeatCreateRequest request) {
        Node node = nodeRepository.findById(request.nodeId())
                .orElseThrow(() -> new CustomException(ErrorCode.NODE_NOT_FOUND));

        Repeat repeat = Repeat.builder()
                .node(node)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .active(request.active())
                .repeatType(request.repeatType())
                .repeatInterval(request.repeatInterval())
                .daysOfWeek(request.daysOfWeek())
                .dayOfMonth(request.dayOfMonth())
                .monthOfYear(request.monthOfYear())
                .status(request.status())
                .build();

        Repeat savedRepeat = repeatRepository.save(repeat);

        return RepeatResponse.from(savedRepeat);
    }

    @Transactional
    public RepeatResponse updateRepeat(Integer routineId, RepeatUpdateRequest request) {
        Repeat repeat = repeatRepository.findById(routineId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPEAT_NOT_FOUND));

        repeat.update(
                request.startDate(),
                request.endDate(),
                request.active(),
                request.repeatType(),
                request.repeatInterval(),
                request.daysOfWeek(),
                request.dayOfMonth(),
                request.monthOfYear(),
                request.status()
        );

        return RepeatResponse.from(repeat);
    }

    @Transactional
    public void deleteRepeat(Integer routineId) {
        Repeat repeat = repeatRepository.findById(routineId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPEAT_NOT_FOUND));

        repeatRepository.delete(repeat);
    }

    @Transactional(readOnly = true)
    public List<RepeatResponse> getRepeats() {
        return repeatRepository.findAll().stream()
                .map(RepeatResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public RepeatResponse getRepeat(Integer repeatId) {
        Repeat repeat = repeatRepository.findById(repeatId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPEAT_NOT_FOUND));

        return RepeatResponse.from(repeat);
    }
}
