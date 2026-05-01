package org.java.inventoryservice.model.dto;

public record SeatResponseDto(
        String seatIdentifier,
        String guid,
        String priceCategoryGuid,
        String rowLabel,
        Integer seatNumber,
        String status
) {}
