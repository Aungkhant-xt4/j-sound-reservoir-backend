package org.java.bookingservice.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "booking_ref", unique = true, nullable = false)
    private String bookingRef;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    private String currency;

    @Column(nullable = false)
    private String status;

    @Column(name = "stripe_session_id")
    private String stripeSessionId;

    @Column(unique = true, updatable = false, nullable = false)
    private String guid;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}
