package org.java.inventoryservice.model.dto;

import java.util.List;

public record BulkSeatCreateRequest(
        Long eventId,
        List<SeatDto> seats
) {
    public record SeatDto(
            String seatIdentifier,
            String priceCategoryGuid,
            String rowLabel,
            Integer seatNumber
    ) {}
}
