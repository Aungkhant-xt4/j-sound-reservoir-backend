package org.java.inventoryservice.model.dto;

public record SeatDetailDto(
    String guid, 
    String section,
    String rowLabel, 
    Integer seatNumber
) {}