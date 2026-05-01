package org.java.eventservice.model.dto.venue;

public record VenueListDto(
        Long id,
        String name,
        String location,
        String maxCapacity
) {}
