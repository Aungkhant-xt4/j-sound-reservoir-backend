package org.java.eventservice.service.venue.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.commonlibrary.exception.BadRequestException;
import org.java.commonlibrary.exception.ResourceNotFoundException;
import org.java.eventservice.model.dto.city.CityDto;
import org.java.eventservice.model.dto.venue.CreateVenueRequest;
import org.java.eventservice.model.dto.venue.VenueDetailDto;
import org.java.eventservice.model.dto.venue.VenueListDto;
import org.java.eventservice.model.entity.City;
import org.java.eventservice.model.entity.Venue;
import org.java.eventservice.repository.CityRepository;
import org.java.eventservice.repository.VenueRepository;
import org.java.eventservice.service.venue.VenueService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;
    private final CityRepository cityRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void createVenue(CreateVenueRequest request) {
        City city = cityRepository.findById(request.cityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + request.cityId()));
        String svgJsonString;
        try {
            Map<String, Object> blueprintMap = new HashMap<>();
            blueprintMap.put("sections", request.sections());
            blueprintMap.put("layout", request.layout());
            svgJsonString = objectMapper.writeValueAsString(blueprintMap);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize venue blueprint", e);
            throw new BadRequestException("Invalid venue layout data format.");
        }
        long totalCapacity = request.layout().seats().stream()
                .filter(seat -> !"killed".equalsIgnoreCase(seat.status()))
                .count();
        String generatedSlug = request.name().toLowerCase().replaceAll("[^a-z0-9]+", "-")
                + "-" + UUID.randomUUID().toString().substring(0, 5);
        Venue newVenue = Venue.builder()
                .name(request.name())
                .city(city)
                .address(request.address())
                .type(request.type())
                .totalCapacity(String.valueOf(totalCapacity))
                .svgJson(svgJsonString)
                .slug(generatedSlug)
                .build();

        venueRepository.save(newVenue);
        log.info("Successfully created venue: {} with capacity {}", newVenue.getName(), totalCapacity);
    }

    @Override
    @Transactional
    public void updateVenue(Long venueId, CreateVenueRequest request) {
        Venue existingVenue = venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with ID: " + venueId));
        City city = cityRepository.findById(request.cityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + request.cityId()));
        String svgJsonString;
        try {
            Map<String, Object> blueprintMap = new HashMap<>();
            blueprintMap.put("sections", request.sections());
            blueprintMap.put("layout", request.layout());

            svgJsonString = objectMapper.writeValueAsString(blueprintMap);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize venue blueprint during update", e);
            throw new BadRequestException("Invalid venue layout data format.");
        }
        long totalCapacity = request.layout().seats().stream()
                .filter(seat -> !"killed".equalsIgnoreCase(seat.status()))
                .count();
        existingVenue.setName(request.name());
        existingVenue.setCity(city);
        existingVenue.setAddress(request.address());
        existingVenue.setType(request.type());
        existingVenue.setTotalCapacity(String.valueOf(totalCapacity));
        existingVenue.setSvgJson(svgJsonString);
        venueRepository.save(existingVenue);
        log.info("Successfully updated venue ID: {} with new capacity {}", venueId, totalCapacity);
    }

    @Override
    public List<VenueListDto> getAllVenues() {
        return venueRepository.findAllVenueSummaries();
    }

    @Override
    public VenueDetailDto getVenueById(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with ID: " + venueId));
        CityDto cityDto = new CityDto(venue.getCity().getId(), venue.getCity().getName());

        return new VenueDetailDto(
                venue.getId(),
                venue.getName(),
                cityDto,
                venue.getAddress(),
                venue.getType(),
                venue.getTotalCapacity(),
                venue.getSvgJson()
        );
    }
}