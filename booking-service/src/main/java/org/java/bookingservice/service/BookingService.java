package org.java.bookingservice.service;

import org.java.bookingservice.model.dto.CreateBookingRequest;

import java.util.List;

public interface BookingService {
    String processBooking(CreateBookingRequest request, Long currentUserId);
}