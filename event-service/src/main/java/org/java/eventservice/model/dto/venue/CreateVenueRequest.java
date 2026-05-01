package org.java.eventservice.model.dto.venue;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateVenueRequest(
        @NotBlank(message = "Venue name is required")
        String name,

        @NotNull(message = "City ID is required")
        Long cityId,

        String address,

        @NotBlank(message = "Venue type is required")
        String type,

        @NotNull(message = "Sections cannot be null")
        List<SectionDto> sections,

        @NotNull(message = "Layout cannot be null")
        LayoutDto layout
) {
    public record SectionDto(String id, String name, String color) {}

    public record LayoutDto(StagePosDto stagePos, List<SeatDto> seats) {}

    public record StagePosDto(int x, int y) {}

    public record SeatDto(String id, String sectionId, String row, String number, double x, double y, String status) {}
}