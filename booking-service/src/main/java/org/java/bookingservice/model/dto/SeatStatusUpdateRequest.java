package org.java.bookingservice.model.dto;

import java.util.List;

public record SeatStatusUpdateRequest(List<String> seatGuids, String status) {}
