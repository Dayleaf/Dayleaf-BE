package org.example.dayleaf.domain.todo.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dayleaf.domain.todo.dto.TodoCreateRequest;
import org.example.dayleaf.domain.todo.dto.TodoResponse;
import org.example.dayleaf.domain.todo.dto.TodoUpdateRequest;
import org.example.dayleaf.domain.todo.dto.YearlyStatsResponse;
import org.example.dayleaf.domain.todo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    // 투두 생성 (Node 동시 생성)
    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(
            @RequestHeader("memberId") Long memberId,
            @RequestBody TodoCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(memberId, request));
    }

    // 투두 수정 (전체 교체)
    @PutMapping("/{todoId}")
    public ResponseEntity<TodoResponse> updateTodo(
            @RequestHeader("memberId") Long memberId,
            @PathVariable Long todoId,
            @RequestBody TodoUpdateRequest request
    ) {
        return ResponseEntity.ok(todoService.updateTodo(memberId, todoId, request));
    }

    // 투두 삭제 (soft delete)
    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(
            @RequestHeader("memberId") Long memberId,
            @PathVariable Long todoId
    ) {
        todoService.deleteTodo(memberId, todoId);
        return ResponseEntity.noContent().build();
    }

    // 투두 목록 조회 (priority → position 순)
    @GetMapping
    public ResponseEntity<List<TodoResponse>> getTodos(
            @RequestHeader("memberId") Long memberId
    ) {
        return ResponseEntity.ok(todoService.getTodos(memberId));
    }

    // 투두 상세 조회
    @GetMapping("/{todoId}")
    public ResponseEntity<TodoResponse> getTodo(
            @RequestHeader("memberId") Long memberId,
            @PathVariable Long todoId
    ) {
        return ResponseEntity.ok(todoService.getTodo(memberId, todoId));
    }

    // 투두 연별 수행률 조회
    @GetMapping("/stats/yearly")
    public ResponseEntity<YearlyStatsResponse> getYearlyStats(
            @RequestHeader("memberId") Long memberId,
            @RequestParam int year
    ) {
        return ResponseEntity.ok(todoService.getYearlyStats(memberId, year));
    }

    // 투두 월별 조회 (yyyy-MM 형식)
    @GetMapping("/by-month/{month}")
    public ResponseEntity<List<TodoResponse>> getTodosByMonth(
            @RequestHeader("memberId") Long memberId,
            @PathVariable String month
    ) {
        return ResponseEntity.ok(todoService.getTodosByMonth(memberId, month));
    }

    // 투두 주별 조회 (yyyy-Www ISO 주차 형식)
    @GetMapping("/by-week/{week}")
    public ResponseEntity<List<TodoResponse>> getTodosByWeek(
            @RequestHeader("memberId") Long memberId,
            @PathVariable String week
    ) {
        return ResponseEntity.ok(todoService.getTodosByWeek(memberId, week));
    }

    // 투두 일별 조회 (yyyy-MM-dd 형식)
    @GetMapping("/by-day/{day}")
    public ResponseEntity<List<TodoResponse>> getTodosByDay(
            @RequestHeader("memberId") Long memberId,
            @PathVariable String day
    ) {
        return ResponseEntity.ok(todoService.getTodosByDay(memberId, day));
    }
}
