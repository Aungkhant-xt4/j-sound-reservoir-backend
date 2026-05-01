package org.java.eventservice.model.dto.event;

import java.util.List;

public record BulkSeatCreateRequest(
        Long eventId,
        List<CreateEventRequest.SeatDto> seats
) {}
