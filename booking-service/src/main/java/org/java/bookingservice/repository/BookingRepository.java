package org.java.bookingservice.repository;

import org.java.bookingservice.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
