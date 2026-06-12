package org.example.dayleaf.domain.todoexecution.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "todo_execution")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_execution_id")
    private Long todoExecutionId;

    @Column(name = "node_id", nullable = false)
    private Long nodeId;

    @Column(name = "node_id2")
    private Long nodeId2;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "is_done", nullable = false)
    private boolean isDone;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false)
    private SourceType sourceType;
}
