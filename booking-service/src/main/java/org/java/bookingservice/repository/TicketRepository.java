package org.java.bookingservice.repository;

import org.java.bookingservice.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByCurrentUserId(Long currentUserId);
}
