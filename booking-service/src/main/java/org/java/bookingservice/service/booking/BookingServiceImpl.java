package org.java.bookingservice.service.booking.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.bookingservice.feignclient.InventoryServiceClient;
import org.java.bookingservice.model.dto.CreateBookingRequest;
import org.java.bookingservice.model.dto.SeatStatusUpdateRequest;
import org.java.bookingservice.model.entity.Booking;
import org.java.bookingservice.model.entity.BookingLedger;
import org.java.bookingservice.model.entity.Ticket;
import org.java.bookingservice.repository.BookingLedgerRepository;
import org.java.bookingservice.repository.BookingRepository;
import org.java.bookingservice.repository.TicketRepository;
import org.java.bookingservice.service.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    private final BookingLedgerRepository ledgerRepository;
    private final InventoryServiceClient inventoryServiceClient;

    @Transactional
    public String processBooking(CreateBookingRequest request, Long currentUserId) {

        log.info("Processing mock payment for method: {}", request.paymentMethodId());
        String bookingRef = "BKG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String bookingGuid = UUID.randomUUID().toString();
        Booking booking = Booking.builder()
                .userId(currentUserId)
                .eventId(request.eventId())
                .bookingRef(bookingRef)
                .guid(bookingGuid)
                .totalAmount(new BigDecimal("150.00"))
                .currency("USD")
                .status("PAID")
                .stripeSessionId("mock_sess_" + UUID.randomUUID().toString().substring(0, 8))
                .build();
        Booking savedBooking = bookingRepository.save(booking);
        BookingLedger ledgerEntry = BookingLedger.builder()
                .booking(savedBooking)
                .stateFrom("NONE")
                .stateTo("PAID")
                .action("CHECKOUT_COMPLETE")
                .rawPayload("{\"paymentMethod\": \"" + request.paymentMethodId() + "\", \"seats\": " + request.seatGuids().size() + "}")
                .build();
        ledgerRepository.save(ledgerEntry);
        List<Ticket> tickets = request.seatGuids().stream()
                .map(guid -> Ticket.builder()
                        .booking(savedBooking)
                        .seatGuid(guid)
                        .ticketHash("TKT-" + UUID.randomUUID())
                        .isScanned(false)
                        .used(false)
                        .currentUserId(currentUserId)
                        .status("ACTIVE")
                        .guid(UUID.randomUUID().toString())
                        .build())
                .collect(Collectors.toList());
        ticketRepository.saveAll(tickets);
        try {
            var updateRequest = new SeatStatusUpdateRequest(request.seatGuids(), "SOLD");
            inventoryServiceClient.updateSeatStatus(updateRequest);
        } catch (Exception e) {
            log.error("Inventory update failed. Rolling back booking.", e);
            throw new RuntimeException("Failed to secure seats. They may have been purchased by another user.");
        }

        log.info("Booking complete! Saved {} tickets for Booking Ref: {}", tickets.size(), bookingRef);
        return bookingRef;
    }
}
