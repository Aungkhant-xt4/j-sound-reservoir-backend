package org.java.eventservice.model.dto.venue;

import org.java.eventservice.model.dto.city.CityDto;

public record VenueDetailDto(
        Long id,
        String name,
        CityDto city,
        String address,
        String type,
        String maxCapacity,
        String svgJson
) {}
