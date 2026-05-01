package org.java.bookingservice.repository;

import org.java.bookingservice.model.entity.BookingLedger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingLedgerRepository extends JpaRepository<BookingLedger, Long> {
}
