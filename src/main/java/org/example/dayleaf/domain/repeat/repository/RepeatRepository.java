package org.example.dayleaf.domain.repeat.repository;

import org.example.dayleaf.domain.repeat.entity.Repeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepeatRepository extends JpaRepository<Repeat, Integer> {
}
