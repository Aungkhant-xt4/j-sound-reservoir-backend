package org.java.eventservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "icon_name")
    private String iconName;

    @Column(unique = true, updatable = false)
    private String guid;
}
