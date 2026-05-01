package org.java.eventservice.model.dto.event;

import java.time.Instant;

public record EventListDto(
        Long id,
        String title,
        String venueName,
        Long availableTicketsCount,
        Instant eventDate,
        Instant availableDate
) {}