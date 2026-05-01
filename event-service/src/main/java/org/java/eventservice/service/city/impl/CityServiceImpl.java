package org.java.eventservice.service.city.impl;

import lombok.RequiredArgsConstructor;
import org.java.eventservice.model.dto.city.CityDto;
import org.java.eventservice.repository.CityRepository;
import org.java.eventservice.service.city.CityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public List<CityDto> getAllCities() {
        return cityRepository.findAll()
                .stream()
                .map(city -> new CityDto(city.getId(), city.getName()))
                .collect(Collectors.toList());
    }
}
