package org.example.dayleaf.domain.todo.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.dayleaf.domain.category.entity.Category;
import org.example.dayleaf.domain.category.repository.CategoryRepository;
import org.example.dayleaf.domain.node.entity.Node;
import org.example.dayleaf.domain.node.entity.NodeType;
import org.example.dayleaf.domain.node.repository.NodeRepository;
import org.example.dayleaf.domain.todo.dto.TodoCreateRequest;
import org.example.dayleaf.domain.todo.dto.TodoResponse;
import org.example.dayleaf.domain.todo.dto.TodoUpdateRequest;
import org.example.dayleaf.domain.todo.dto.YearlyStatsResponse;
import org.example.dayleaf.domain.todo.dto.YearlyStatsResponse.MonthStats;
import org.example.dayleaf.domain.todo.entity.Priority;
import org.example.dayleaf.domain.todo.entity.Todo;
import org.example.dayleaf.domain.todo.entity.TodoStatus;
import org.example.dayleaf.domain.todo.repository.TodoRepository;
import org.example.dayleaf.domain.todoexecution.repository.TodoExecutionRepository;
import org.example.dayleaf.global.exception.CustomException;
import org.example.dayleaf.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final NodeRepository nodeRepository;
    private final CategoryRepository categoryRepository;
    private final TodoExecutionRepository todoExecutionRepository;

    // 투두 생성 (Node 동시 생성, 같은 트랜잭션)
    @Transactional
    public TodoResponse createTodo(Long memberId, TodoCreateRequest request) {
        // 부모 노드 조회 및 depth/position 계산
        Node parent = null;
        int depth = 0;
        int position;

        if (request.parentId() != null) {
            parent = nodeRepository.findById(request.parentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.NODE_NOT_FOUND));
            depth = parent.getDepth() + 1;
            position = nodeRepository.findMaxPositionByParentId(request.parentId()) + 1;
        } else {
            position = nodeRepository.findMaxPositionByMemberIdAndParentIsNull(memberId) + 1;
        }

        // Node 생성 (path는 저장 후 업데이트)
        Node node = Node.builder()
                .parent(parent)
                .memberId(memberId)
                .title(request.title())
                .type(NodeType.PAGE)
                .depth(depth)
                .path(null)
                .position(position)
                .archived(false)
                .build();

        Node savedNode = nodeRepository.save(node);

        // path = parent.path + "/" + savedNode.id (최상위이면 savedNode.id만)
        String path = parent != null && parent.getPath() != null
                ? parent.getPath() + "/" + savedNode.getId()
                : String.valueOf(savedNode.getId());

        nodeRepository.updatePath(savedNode.getId(), path);

        // 카테고리 조회 (nullable)
        Category category = null;
        if (request.categoryId() != null) {
            category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        }

        // Todo 생성
        Todo todo = Todo.builder()
                .node(savedNode)
                .category(category)
                .priority(request.priority() != null ? request.priority() : Priority.MEDIUM)
                .status(TodoStatus.ACTIVE)
                .build();

        Todo savedTodo = todoRepository.save(todo);
        return TodoResponse.from(savedTodo);
    }

    // 투두 수정 (전체 교체: title, priority, categoryId)
    @Transactional
    public TodoResponse updateTodo(Long memberId, Long todoId, TodoUpdateRequest request) {
        Todo todo = findTodoWithAuth(memberId, todoId);

        // Node title 업데이트 (Node 엔티티 수정 불가로 인해 JPQL 사용)
        nodeRepository.updateTitle(todo.getId(), request.title());

        // 우선순위 업데이트
        todo.updatePriority(request.priority());

        // 카테고리 업데이트
        Integer newCategoryId = null;
        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
            todo.assignCategory(category);
            newCategoryId = category.getId();
        } else {
            todo.removeCategory();
        }

        // JPQL UPDATE 후 1차 캐시에 이전 title이 남으므로 직접 응답 구성
        return new TodoResponse(todo.getId(), request.title(), todo.getPriority(), todo.getStatus(), newCategoryId);
    }

    // 투두 삭제 (soft delete: Node.is_archived = true)
    @Transactional
    public void deleteTodo(Long memberId, Long todoId) {
        findTodoWithAuth(memberId, todoId);

        // Node soft delete (Node 엔티티 수정 불가로 인해 JPQL 사용)
        nodeRepository.archiveById(todoId);
    }

    // 투두 목록 조회 (priority → position 순)
    public List<TodoResponse> getTodos(Long memberId) {
        List<Todo> todos = todoRepository.findAllByMemberIdAndNotArchived(memberId);
        return sortAndMap(todos);
    }

    // 투두 상세 조회
    public TodoResponse getTodo(Long memberId, Long todoId) {
        Todo todo = findTodoWithAuth(memberId, todoId);
        return TodoResponse.from(todo);
    }

    // 투두 월별 조회 (yyyy-MM 형식)
    public List<TodoResponse> getTodosByMonth(Long memberId, String month) {
        YearMonth yearMonth = YearMonth.parse(month, DateTimeFormatter.ofPattern("yyyy-MM"));
        LocalDate from = yearMonth.atDay(1);
        LocalDate to = yearMonth.atEndOfMonth();
        return getTodosByDateRange(memberId, from, to);
    }

    // 투두 주별 조회 (yyyy-Www ISO 주차 형식, 예: 2025-W23)
    public List<TodoResponse> getTodosByWeek(Long memberId, String week) {
        // "2025-W23" + "-1" → ISO 8601 주차의 월요일 파싱
        LocalDate startOfWeek = LocalDate.parse(week + "-1", DateTimeFormatter.ISO_WEEK_DATE);
        LocalDate endOfWeek = startOfWeek.with(DayOfWeek.SUNDAY);
        return getTodosByDateRange(memberId, startOfWeek, endOfWeek);
    }

    // 투두 일별 조회 (yyyy-MM-dd 형식)
    public List<TodoResponse> getTodosByDay(Long memberId, String day) {
        LocalDate date = LocalDate.parse(day);
        return getTodosByDateRange(memberId, date, date);
    }

    // 연별 수행률 조회
    public YearlyStatsResponse getYearlyStats(Long memberId, int year) {
        // 회원의 전체 활성 투두 nodeId 목록
        List<Todo> allTodos = todoRepository.findAllByMemberIdAndNotArchived(memberId);
        List<Long> nodeIds = allTodos.stream()
                .map(t -> t.getNode().getId())
                .collect(Collectors.toList());

        int totalCount = nodeIds.size();

        // 연도별 월별 완료 투두 수 조회
        Map<Integer, Long> completedByMonth = new HashMap<>();
        if (!nodeIds.isEmpty()) {
            List<Object[]> results = todoExecutionRepository.countCompletedByMonthForYear(nodeIds, year);
            for (Object[] row : results) {
                int monthNum = ((Number) row[0]).intValue();
                long count = ((Number) row[1]).longValue();
                completedByMonth.put(monthNum, count);
            }
        }

        // 12개월 통계 생성
        List<MonthStats> months = new ArrayList<>();
        for (int m = 1; m <= 12; m++) {
            int completed = completedByMonth.getOrDefault(m, 0L).intValue();
            double rate = totalCount > 0 ? Math.round((double) completed / totalCount * 1000.0) / 10.0 : 0.0;
            months.add(new MonthStats(m, totalCount, completed, rate));
        }

        return new YearlyStatsResponse(year, months);
    }

    // 날짜 범위로 투두 조회 (TodoExecution 기준)
    private List<TodoResponse> getTodosByDateRange(Long memberId, LocalDate from, LocalDate to) {
        List<Todo> allTodos = todoRepository.findAllByMemberIdAndNotArchived(memberId);
        if (allTodos.isEmpty()) {
            return List.of();
        }

        List<Long> allNodeIds = allTodos.stream()
                .map(t -> t.getNode().getId())
                .collect(Collectors.toList());

        // 해당 기간에 실행 기록이 있는 nodeId 목록
        List<Long> executedNodeIds = todoExecutionRepository
                .findExecutedNodeIdsByNodeIdsAndDateBetween(allNodeIds, from, to);

        if (executedNodeIds.isEmpty()) {
            return List.of();
        }

        List<Todo> todos = todoRepository.findAllByNodeIdIn(executedNodeIds);
        return sortAndMap(todos);
    }

    // priority → position 순 정렬 후 DTO 변환
    private List<TodoResponse> sortAndMap(List<Todo> todos) {
        Map<Priority, Integer> priorityOrder = Map.of(
                Priority.HIGH, 0,
                Priority.MEDIUM, 1,
                Priority.LOW, 2
        );

        return todos.stream()
                .sorted(Comparator
                        .comparingInt((Todo t) -> priorityOrder.getOrDefault(t.getPriority(), 3))
                        .thenComparingInt(t -> t.getNode().getPosition()))
                .map(TodoResponse::from)
                .collect(Collectors.toList());
    }

    // 투두 조회 + 권한 검증 (내부 헬퍼)
    private Todo findTodoWithAuth(Long memberId, Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));

        if (!todo.getNode().getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.TODO_ACCESS_DENIED);
        }

        return todo;
    }
}
