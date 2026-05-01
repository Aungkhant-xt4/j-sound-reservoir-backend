package org.java.eventservice.model.dto.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record EventFrontDetailDto(
        Long id,
        String title,
        String description,
        Instant eventDate,
        String image,
        VenueInfoDto venue,
        LayoutDto layout
) {
    public record VenueInfoDto(Long id, String name, String location) {}

    public record LayoutDto(
            StagePosDto stagePos,
            Integer canvasWidth,
            Integer canvasHeight,
            List<PriceCategoryDto> priceCategories,
            List<SeatWithCoordinatesDto> seats
    ) {}

    public record StagePosDto(Integer x, Integer y) {}

    public record PriceCategoryDto(String guid, String name, BigDecimal price, String colorCode) {}

    public record SeatWithCoordinatesDto(
            String id,
            String guid,
            Integer x,
            Integer y,
            String row,
            String number,
            String status,
            String priceCategoryGuid
    ) {}
}
