package org.java.eventservice.model.dto.event;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record CreateEventRequest(
        @NotBlank(message = "Title is required")
        String title,

        String description,

        @NotNull(message = "Venue ID is required")
        Long venueId,

        @NotNull(message = "Event date is required")
        Instant eventDate,

        @NotNull(message = "Available date is required")
        Instant availableDate,

        String imageUrl,

        @NotBlank(message = "Event type is required")
        String type,

        @NotNull
        List<PriceCategoryDto> priceCategories,

        @NotNull
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
            String priceCategoryGuid,
            String rowLabel,
            Integer seatNumber
    ) {}
}
