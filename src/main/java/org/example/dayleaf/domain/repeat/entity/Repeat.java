package org.example.dayleaf.domain.repeat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.dayleaf.domain.node.entity.Node;

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
    private boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "repeat_type", nullable = false)
    private org.example.dayleaf.domain.repeat.entity.RepeatType repeatType;

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
    private org.example.dayleaf.domain.repeat.entity.RepeatStatus status;

    @Builder
    private Repeat(
            Node node,
            LocalDate startDate,
            LocalDate endDate,
            boolean active,
            org.example.dayleaf.domain.repeat.entity.RepeatType repeatType,
            Integer repeatInterval,
            String daysOfWeek,
            Integer dayOfMonth,
            Integer monthOfYear,
            org.example.dayleaf.domain.repeat.entity.RepeatStatus status
    ) {
        this.node = node;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
        this.repeatType = repeatType;
        this.repeatInterval = repeatInterval;
        this.daysOfWeek = daysOfWeek;
        this.dayOfMonth = dayOfMonth;
        this.monthOfYear = monthOfYear;
        this.status = status;
    }

    public void update(
            LocalDate startDate,
            LocalDate endDate,
            boolean active,
            org.example.dayleaf.domain.repeat.entity.RepeatType repeatType,
            Integer repeatInterval,
            String daysOfWeek,
            Integer dayOfMonth,
            Integer monthOfYear,
            org.example.dayleaf.domain.repeat.entity.RepeatStatus status
    ) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
        this.repeatType = repeatType;
        this.repeatInterval = repeatInterval;
        this.daysOfWeek = daysOfWeek;
        this.dayOfMonth = dayOfMonth;
        this.monthOfYear = monthOfYear;
        this.status = status;
    }
}
