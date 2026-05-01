package org.java.bookingservice.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    @Column(name = "seat_guid", nullable = false)
    private String seatGuid;

    @Column(name = "ticket_hash", unique = true)
    private String ticketHash;

    @Column(name = "is_scanned")
    private Boolean isScanned;

    @Column(name = "current_user_id")
    private Long currentUserId;

    private String status;

    @Column(name = "transfer_token")
    private String transferToken;

    private Boolean used;

    @Column(unique = true, updatable = false, nullable = false)
    private String guid;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}
