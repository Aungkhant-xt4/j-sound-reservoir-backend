package org.java.bookingservice.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "booking_ledger")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingLedger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "state_from")
    private String stateFrom;

    @Column(name = "state_to")
    private String stateTo;

    @Column(name = "raw_payload", columnDefinition = "TEXT")
    private String rawPayload;

    private String action;

    @CreationTimestamp
    private Instant timestamp;
}
