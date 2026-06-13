package org.example.dayleaf.domain.todo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.dayleaf.domain.category.entity.Category;
import org.example.dayleaf.domain.node.entity.Node;
import org.example.dayleaf.global.base.BaseEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends BaseEntity {

    // node_id를 PK로 공유 (1:1 구조)
    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id")
    private Node node;

    // 카테고리 (nullable)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // 우선순위
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    // 사용 상태 (완료 여부 아님 — 완료는 TodoExecution으로 관리)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus status;

    @Builder
    private Todo(Node node, Category category, Priority priority, TodoStatus status) {
        this.node = node;
        this.category = category;
        this.priority = priority;
        this.status = status != null ? status : TodoStatus.ACTIVE;
    }

    // 우선순위 변경
    public void updatePriority(Priority priority) {
        this.priority = priority;
    }

    // 사용 상태 변경
    public void updateStatus(TodoStatus status) {
        this.status = status;
    }

    // 카테고리 지정
    public void assignCategory(Category category) {
        this.category = category;
    }

    // 카테고리 해제
    public void removeCategory() {
        this.category = null;
    }
}
