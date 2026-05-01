package org.java.bookingservice.service.ticket;

import org.java.bookingservice.model.dto.UserTicketResponseDto;

import java.util.List;

public interface TicketService {
    List<UserTicketResponseDto> getMyTickets(Long currentUserId);
}
