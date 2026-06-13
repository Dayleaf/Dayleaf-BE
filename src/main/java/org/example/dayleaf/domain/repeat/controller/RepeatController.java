package org.example.dayleaf.domain.repeat.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dayleaf.domain.repeat.dto.request.RepeatCreateRequest;
import org.example.dayleaf.domain.repeat.dto.request.RepeatUpdateRequest;
import org.example.dayleaf.domain.repeat.dto.response.RepeatResponse;
import org.example.dayleaf.domain.repeat.service.RepeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repeats")
@RequiredArgsConstructor
public class RepeatController {

    private final RepeatService repeatService;

    @GetMapping
    public ResponseEntity<List<RepeatResponse>> getRepeats() {
        List<RepeatResponse> response = repeatService.getRepeats();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{repeatId}")
    public ResponseEntity<RepeatResponse> getRepeat(
            @PathVariable Integer repeatId
    ) {
        RepeatResponse response = repeatService.getRepeat(repeatId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<RepeatResponse> createRepeat(
            @RequestBody RepeatCreateRequest request
    ) {
        RepeatResponse response = repeatService.createRepeat(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{routineId}")
    public ResponseEntity<RepeatResponse> updateRepeat(
            @PathVariable Integer routineId,
            @RequestBody RepeatUpdateRequest request
    ) {
        RepeatResponse response = repeatService.updateRepeat(routineId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{routineId}")
    public ResponseEntity<Void> deleteRepeat(
            @PathVariable Integer routineId
    ) {
        repeatService.deleteRepeat(routineId);
        return ResponseEntity.noContent().build();
    }
}
