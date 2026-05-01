package org.java.eventservice.repository;

import org.java.eventservice.model.dto.venue.VenueListDto;
import org.java.eventservice.model.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VenueRepository extends JpaRepository<Venue, Long> {

    @Query("""
            SELECT new org.java.eventservice.model.dto.venue.VenueListDto(
                v.id, 
                v.name, 
                v.city.name, 
                v.totalCapacity
            ) 
            FROM Venue v
            """)
    List<VenueListDto> findAllVenueSummaries();
}
