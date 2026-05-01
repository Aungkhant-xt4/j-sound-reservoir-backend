package org.java.bookingservice.model.dto;

import java.time.Instant;

public record UserTicketResponseDto(
        String ticketGuid,
        String bookingRef,
        String ticketHash,
        String status,
        Boolean isScanned,
        EventSummaryDto event,
        SeatSummaryDto seat
) {
    public record EventSummaryDto(
            Long id,
            String title,
            String tour,
            Instant eventDate,
            String venueName,
            String location,
            String imageUrl
    ) {}

    public record SeatSummaryDto(
            String guid,
            String section,
            String row,
            String number
    ) {}
}