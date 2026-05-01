package org.java.inventoryservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.java.inventoryservice.model.enumration.SeatStatus;

import java.time.Instant;

@Entity
@Table(name = "seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private Long eventId;
    @Column(name = "price_category_guid", nullable = false)
    private String priceCategoryGuid;

    @Column(name = "seat_name", nullable = false)
    private String seatName;

    @Column(name = "row_label")
    private String rowLabel;

    @Column(name = "seat_number")
    private Integer seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status;

    @Version
    private Integer version;

    @Column(name = "locked_until")
    private Instant lockedUntil;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(nullable = false, unique = true, updatable = false)
    private String guid;
}
