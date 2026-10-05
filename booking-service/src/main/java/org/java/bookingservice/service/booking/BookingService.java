package org.java.bookingservice.service.booking;

import org.java.bookingservice.model.dto.CreateBookingRequest;

public interface BookingService {
    String processBooking(CreateBookingRequest request, Long currentUserId);
}