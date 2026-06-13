package org.example.dayleaf.domain.schedule.repository;

import java.time.LocalDate;
import java.util.List;
import org.example.dayleaf.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByDate(LocalDate date);

    List<Schedule> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
