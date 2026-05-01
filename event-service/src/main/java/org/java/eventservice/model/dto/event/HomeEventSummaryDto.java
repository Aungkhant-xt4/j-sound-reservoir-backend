package org.java.eventservice.model.dto.event;

public record HomeEventSummaryDto(
        Long id,
        String title,
        String description,
        String eventDate,
        String venueName,
        String address,
        String eventCategory,
        Long availableTicketsCount,
        String imageUrl 
) {}