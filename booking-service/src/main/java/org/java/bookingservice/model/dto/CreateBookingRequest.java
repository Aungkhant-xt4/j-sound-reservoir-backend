package org.java.bookingservice.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateBookingRequest(

        @NotNull(message = "Event ID is required")
        Long eventId,

        @NotEmpty(message = "At least one seat must be selected")
        List<String> seatGuids,

        @NotBlank(message = "Payment method token is required")
        String paymentMethodId
) {}