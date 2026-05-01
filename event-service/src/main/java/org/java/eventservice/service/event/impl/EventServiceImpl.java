package org.java.eventservice.service.event.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.commonlibrary.exception.ExternalServiceException;
import org.java.commonlibrary.exception.ResourceNotFoundException;
import org.java.eventservice.feignclient.inventory.InventoryServiceClient;
import org.java.eventservice.model.dto.event.*;
import org.java.eventservice.model.dto.venue.VenueBlueprint;
import org.java.eventservice.model.entity.Event;
import org.java.eventservice.model.entity.PriceCategory;
import org.java.eventservice.model.entity.Venue;
import org.java.eventservice.repository.EventRepository;
import org.java.eventservice.repository.PriceCategoryRepository;
import org.java.eventservice.repository.VenueRepository;
import org.java.eventservice.service.event.EventService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final PriceCategoryRepository priceCategoryRepository;
    private final InventoryServiceClient inventoryServiceClient;

    @Override
    @Transactional
    public void createEvent(CreateEventRequest request) {
        Venue venue = venueRepository.findById(request.venueId())
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with ID: " + request.venueId()));
        String eventGuid = UUID.randomUUID().toString();
        String slug = request.title().toLowerCase().replaceAll("[^a-z0-9]+", "-")
                + "-" + eventGuid.substring(0, 5);
        Event newEvent = Event.builder()
                .title(request.title())
                .description(request.description())
                .venue(venue)
                .eventDate(request.eventDate())
                .availableDate(request.availableDate())
                .imageUrl(request.imageUrl())
                .type(request.type())
                .guid(eventGuid)
                .slug(slug)
                .availableTicketsCount((long) request.seats().size())
                .build();

        Event savedEvent = eventRepository.save(newEvent);
        List<PriceCategory> categoryEntities = request.priceCategories().stream()
                .map(dto -> PriceCategory.builder()
                        .event(savedEvent)
                        .name(dto.name())
                        .price(dto.price())
                        .colorCode(dto.colorCode())
                        .guid(dto.guid())
                        .build())
                .collect(Collectors.toList());

        priceCategoryRepository.saveAll(categoryEntities);
        try {
            BulkSeatCreateRequest feignPayload = new BulkSeatCreateRequest(
                    savedEvent.getId(),
                    request.seats()
            );

            log.info("Sending {} seats to inventory-service for event {}", request.seats().size(), savedEvent.getId());
            inventoryServiceClient.createSeatsInBulk(feignPayload);

        } catch (Exception e) {
            log.error("Failed to synchronize seats with inventory-service", e);
            throw new ExternalServiceException("Failed to initialize seating inventory. Event creation aborted.");
        }

        log.info("Successfully created event: {}", savedEvent.getTitle());
    }

    @Override
    @Transactional
    public void updateEvent(Long eventId, CreateEventRequest request) {
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + eventId));

        Venue venue = venueRepository.findById(request.venueId())
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with ID: " + request.venueId()));
        existingEvent.setTitle(request.title());
        existingEvent.setDescription(request.description());
        existingEvent.setVenue(venue);
        existingEvent.setEventDate(request.eventDate());
        existingEvent.setAvailableDate(request.availableDate());
        existingEvent.setImageUrl(request.imageUrl());
        existingEvent.setType(request.type());
        existingEvent.setAvailableTicketsCount((long) request.seats().size());

        eventRepository.save(existingEvent);
        List<PriceCategory> existingCategories = priceCategoryRepository.findByEventId(eventId);
        Map<String, PriceCategory> existingCategoryMap = existingCategories.stream()
                .collect(Collectors.toMap(PriceCategory::getGuid, pc -> pc));

        List<PriceCategory> categoriesToSave = new ArrayList<>();

        for (CreateEventRequest.PriceCategoryDto dto : request.priceCategories()) {
            if (existingCategoryMap.containsKey(dto.guid())) {
                PriceCategory categoryToUpdate = existingCategoryMap.get(dto.guid());
                categoryToUpdate.setName(dto.name());
                categoryToUpdate.setPrice(dto.price());
                categoryToUpdate.setColorCode(dto.colorCode());
                categoriesToSave.add(categoryToUpdate);
                existingCategoryMap.remove(dto.guid());
            } else {
                categoriesToSave.add(PriceCategory.builder()
                        .event(existingEvent)
                        .name(dto.name())
                        .price(dto.price())
                        .colorCode(dto.colorCode())
                        .guid(dto.guid())
                        .build());
            }
        }
        priceCategoryRepository.deleteAll(existingCategoryMap.values());
        priceCategoryRepository.saveAll(categoriesToSave);
        try {
            BulkSeatCreateRequest feignPayload = new BulkSeatCreateRequest(eventId, request.seats());
            log.info("Synchronizing {} seats with inventory-service for event {}", request.seats().size(), eventId);

            inventoryServiceClient.updateSeatsInBulk(feignPayload);
        } catch (Exception e) {
            log.error("Failed to synchronize updated seats with inventory-service", e);
            throw new ExternalServiceException("Failed to synchronize seating inventory. Event update aborted.");
        }

        log.info("Successfully updated event: {}", existingEvent.getTitle());
    }

    @Override
    public List<EventListDto> getAllEvents() {
        return eventRepository.findAllEventSummaries();
    }

    @Override
    public EventDetailDto getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + eventId));
        List<EventDetailDto.PriceCategoryDto> categories = priceCategoryRepository.findByEventId(eventId).stream()
                .map(pc -> new EventDetailDto.PriceCategoryDto(
                        pc.getGuid(),
                        pc.getName(),
                        pc.getPrice(),
                        pc.getColorCode()
                ))
                .toList();
        List<EventDetailDto.SeatDto> seats;
        try {
            var response = inventoryServiceClient.getSeatsByEventId(eventId);
            seats = response.data();
        } catch (Exception e) {
            log.error("Failed to fetch seats for event {} from inventory-service", eventId, e);
            throw new ExternalServiceException("Could not load seat map from inventory service.");
        }
        return new EventDetailDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getVenue().getId(),
                event.getEventDate(),
                event.getAvailableDate(),
                event.getImageUrl(),
                event.getType(),
                categories,
                seats
        );
    }
    @Override
    public EventFrontDetailDto getEventForFront(Long eventId) {
        ObjectMapper objectMapper = new ObjectMapper();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        List<EventFrontDetailDto.PriceCategoryDto> categories = priceCategoryRepository.findByEventId(eventId).stream()
                .map(pc -> new EventFrontDetailDto.PriceCategoryDto(
                        pc.getGuid(), pc.getName(), pc.getPrice(), pc.getColorCode()
                )).toList();
        List<EventDetailDto.SeatDto> inventorySeats = inventoryServiceClient.getSeatsByEventId(eventId).data();
        VenueBlueprint blueprint;
        try {
            String rawJson = event.getVenue().getSvgJson();
            if (rawJson.startsWith("\"") && rawJson.endsWith("\"")) {
                rawJson = objectMapper.readValue(rawJson, String.class);
            }
            blueprint = objectMapper.readValue(rawJson, VenueBlueprint.class);
        } catch (Exception e) {
            log.error("Failed to parse venue blueprint for event {}", eventId, e);
            throw new RuntimeException("Could not load venue layout.");
        }
        Map<String, VenueBlueprint.PhysicalSeat> physicalSeatMap = blueprint.layout.seats.stream()
                .collect(Collectors.toMap(s -> s.id, s -> s));
        List<EventFrontDetailDto.SeatWithCoordinatesDto> mergedSeats = inventorySeats.stream()
                .map(invSeat -> {
                    VenueBlueprint.PhysicalSeat physSeat = physicalSeatMap.get(invSeat.seatIdentifier());

                    return new EventFrontDetailDto.SeatWithCoordinatesDto(
                            invSeat.seatIdentifier(),
                            invSeat.guid(),
                            physSeat != null ? physSeat.x : 0,
                            physSeat != null ? physSeat.y : 0,
                            invSeat.rowLabel(),
                            invSeat.seatNumber() != null ? String.valueOf(invSeat.seatNumber()) : "",
                            invSeat.status(),
                            invSeat.priceCategoryGuid()
                    );
                }).toList();
        EventFrontDetailDto.LayoutDto layout = new EventFrontDetailDto.LayoutDto(
                blueprint.layout.stagePos,
                blueprint.layout.canvasWidth != null ? blueprint.layout.canvasWidth : 1000,
                blueprint.layout.canvasHeight != null ? blueprint.layout.canvasHeight : 1000,
                categories,
                mergedSeats
        );
        return new EventFrontDetailDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getEventDate(),
                event.getImageUrl(),
                new EventFrontDetailDto.VenueInfoDto(
                        event.getVenue().getId(),
                        event.getVenue().getName(),
                        event.getVenue().getCity().getName()
                ),
                layout
        );
    }

    @Override
    public BasicEventDto getBasicEventById(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        return new BasicEventDto(
                event.getId(),
                event.getTitle(),
                event.getType(),
                event.getEventDate().toString(),
                event.getImageUrl(),
                new BasicEventDto.VenueDto(
                        event.getVenue().getName(),
                        event.getVenue().getCity().getName()
                )
        );
    }

    @Override
    public Map<String, List<HomeEventSummaryDto>> getHomePageEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(event -> new HomeEventSummaryDto(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getEventDate() != null ? event.getEventDate().toString() : null,
                        event.getVenue() != null ? event.getVenue().getName() : "TBA",
                        event.getVenue() != null ? event.getVenue().getAddress() : "TBA",
                        event.getEventCategory() != null ? event.getEventCategory().getName() : "Uncategorized",
                        event.getAvailableTicketsCount(),
                        event.getImageUrl()
                ))
                .collect(Collectors.groupingBy(HomeEventSummaryDto::eventCategory));
    }
}
