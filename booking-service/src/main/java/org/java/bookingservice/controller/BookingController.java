package org.java.bookingservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.bookingservice.feignclient.AuthServiceClient;
import org.java.bookingservice.model.dto.CreateBookingRequest;
import org.java.bookingservice.service.BookingService;
import org.java.commonlibrary.model.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;
    private final AuthServiceClient authServiceClient;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createBooking(
            @Valid @RequestBody CreateBookingRequest request,
            Principal principal) {

        log.info("Received booking request for Event ID: {} with {} seats.",
                request.eventId(), request.seatGuids().size());
        String email = principal.getName();

        Long currentUserId = authServiceClient.getUserIdByEmail(email).data();
        String orderRef = bookingService.processBooking(request, currentUserId);

        return ResponseEntity.ok(ApiResponse.success("Payment Successful! Order Ref: " + orderRef));
    }
}