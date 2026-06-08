package org.example.dayleaf.domain.repeat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.dayleaf.domain.node.entity.Node;

import java.time.LocalDate;

@Entity
@Table(name = "repeat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Repeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_id")
    private Integer routineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id", nullable = false)
    private Node node;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "repeat_type", nullable = false)
    private RepeatType repeatType;

    @Column(name = "repeat_interval", nullable = false)
    private Integer repeatInterval;

    @Column(name = "days_of_week")
    private String daysOfWeek;

    @Column(name = "day_of_month")
    private Integer dayOfMonth;

    @Column(name = "month_of_year")
    private Integer monthOfYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RepeatStatus status;
}
