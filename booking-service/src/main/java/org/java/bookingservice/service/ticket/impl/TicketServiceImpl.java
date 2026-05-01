package org.java.bookingservice.service.ticket.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.bookingservice.feignclient.EventServiceClient;
import org.java.bookingservice.feignclient.InventoryServiceClient;
import org.java.bookingservice.model.dto.UserTicketResponseDto;
import org.java.bookingservice.model.entity.Ticket;
import org.java.bookingservice.repository.TicketRepository;
import org.java.bookingservice.service.ticket.TicketService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final EventServiceClient eventServiceClient;
    private final InventoryServiceClient inventoryServiceClient;

    @Override
    public List<UserTicketResponseDto> getMyTickets(Long currentUserId) {
        List<Ticket> tickets = ticketRepository.findByCurrentUserId(currentUserId);

        if (tickets.isEmpty()) {
            return List.of();
        }
        List<String> seatGuids = tickets.stream().map(Ticket::getSeatGuid).distinct().toList();

        Map<String, InventoryServiceClient.SeatDetailDto> seatMap = inventoryServiceClient
                .getSeatsByGuids(seatGuids).data().stream()
                .collect(Collectors.toMap(InventoryServiceClient.SeatDetailDto::guid, seat -> seat));
        return tickets.stream().map(ticket -> {
            var event = eventServiceClient.getEventById(ticket.getBooking().getEventId()).data();
            var seat = seatMap.get(ticket.getSeatGuid());

            return new UserTicketResponseDto(
                    ticket.getGuid(),
                    ticket.getBooking().getBookingRef(),
                    ticket.getTicketHash(),
                    "VALID TICKET",
                    ticket.getIsScanned(),
                    new UserTicketResponseDto.EventSummaryDto(
                            event.id(),
                            event.title(),
                            event.type(),
                            Instant.parse(event.eventDate()),
                            event.venue().name(),
                            event.venue().location(),
                            event.imageUrl()
                    ),
                    new UserTicketResponseDto.SeatSummaryDto(
                            seat.guid(),
                            seat.priceCategoryName(),
                            seat.rowLabel(),
                            String.valueOf(seat.seatNumber())
                    )
            );
        }).toList();
    }
}
