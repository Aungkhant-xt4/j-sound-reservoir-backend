package org.java.eventservice.model.dto.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record EventDetailDto(
        Long id,
        String title,
        String description,
        Long venueId,
        Instant eventDate,
        Instant availableDate,
        String imageUrl,
        String type,
        List<PriceCategoryDto> priceCategories,
        List<SeatDto> seats
) {
    public record PriceCategoryDto(
            String guid,
            String name,
            BigDecimal price,
            String colorCode
    ) {}

    public record SeatDto(
            String seatIdentifier,
            String guid,
            String priceCategoryGuid,
            String rowLabel,
            Integer seatNumber,
            String status
    ) {}
}
