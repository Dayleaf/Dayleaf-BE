package org.example.dayleaf.domain.schedule.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.dayleaf.domain.node.entity.Node;

@Entity
@Table(name = "schedule")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id
    @Column(name = "node_id")
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id")
    private Node node;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "is_all_day", nullable = false)
    private boolean allDay;

    @Builder
    private Schedule(Node node, LocalDate date, LocalTime startTime, LocalTime endTime, boolean allDay) {
        this.node = node;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.allDay = allDay;
    }

    public void update(LocalDate date, LocalTime startTime, LocalTime endTime, boolean allDay) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.allDay = allDay;
    }
}
