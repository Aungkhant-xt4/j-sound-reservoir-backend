package org.java.eventservice.repository;

import org.java.eventservice.model.dto.event.EventListDto;
import org.java.eventservice.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("""
            SELECT new org.java.eventservice.model.dto.event.EventListDto(
                e.id, 
                e.title, 
                v.name, 
                e.availableTicketsCount, 
                e.eventDate, 
                e.availableDate
            ) 
            FROM Event e 
            JOIN e.venue v
            ORDER BY e.eventDate DESC
            """)
    List<EventListDto> findAllEventSummaries();
}
