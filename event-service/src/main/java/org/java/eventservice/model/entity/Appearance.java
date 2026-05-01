package org.java.eventservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "appearance",
        indexes = {
                @Index(name = "idx_appearance_talent_id",columnList = "talent_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"event_id", "talent_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appearance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "talent_id", nullable = false)
    private Long talentId;

    @Column(name = "participation_type")
    private String participationType;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "appearance_time")
    private Instant appearanceTime;
}
