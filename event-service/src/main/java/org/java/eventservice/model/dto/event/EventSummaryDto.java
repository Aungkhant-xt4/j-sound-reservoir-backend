package org.java.eventservice.model.dto.event;

import lombok.Builder;

@Builder
public record EventSummaryDto(
        Long id,
        String eventName,
        int availableSeats,
        String status
) {}
