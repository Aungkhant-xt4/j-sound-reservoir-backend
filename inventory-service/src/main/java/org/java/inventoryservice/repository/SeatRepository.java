package org.java.inventoryservice.repository;

import org.java.inventoryservice.model.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByEventId(Long eventId);
    List<Seat> findByGuidIn(List<String> guids);
}
