package org.java.eventservice.repository;

import org.java.eventservice.model.entity.PriceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceCategoryRepository extends JpaRepository<PriceCategory, Long> {
    List<PriceCategory> findByEventId(Long eventId);
}
