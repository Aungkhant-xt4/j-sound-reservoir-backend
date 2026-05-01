package org.java.bookingservice.controller;

import lombok.RequiredArgsConstructor;
import org.java.bookingservice.feignclient.AuthServiceClient;
import org.java.bookingservice.model.dto.UserTicketResponseDto;
import org.java.bookingservice.service.ticket.TicketService;
import org.java.commonlibrary.model.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final AuthServiceClient authServiceClient;

    @GetMapping("/my-tickets")
    public ResponseEntity<ApiResponse<List<UserTicketResponseDto>>> getMyTickets(Principal principal) {
        Long userId = authServiceClient.getUserIdByEmail(principal.getName()).data();
        
        List<UserTicketResponseDto> tickets = ticketService.getMyTickets(userId);
        
        return ResponseEntity.ok(ApiResponse.success(tickets));
    }
}