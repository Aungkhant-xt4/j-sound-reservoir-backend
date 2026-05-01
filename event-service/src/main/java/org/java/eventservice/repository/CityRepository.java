package org.java.eventservice.repository;

import org.java.eventservice.model.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findById(Long id);

    List<City> findAll();
}
