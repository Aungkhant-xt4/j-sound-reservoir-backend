package org.java.inventoryservice.model.dto;

import java.util.List;

public record SeatStatusUpdateRequest(List<String> seatGuids, String status) {}
