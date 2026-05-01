package org.java.eventservice.controller;

import lombok.RequiredArgsConstructor;
import org.java.commonlibrary.model.dto.ApiResponse;
import org.java.eventservice.model.dto.event.*;
import org.java.eventservice.service.event.EventService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> createEvent(@Valid @RequestBody CreateEventRequest request) {

        eventService.createEvent(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Event and seating inventory created successfully."));
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody CreateEventRequest request) {

        eventService.updateEvent(eventId, request);

        return ResponseEntity.ok(
                ApiResponse.success("Event and seating inventory updated successfully.")
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<EventListDto>>> getAllEvents() {

        List<EventListDto> events = eventService.getAllEvents();

        return ResponseEntity.ok(ApiResponse.success(events));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventDetailDto>> getEventById(@PathVariable Long eventId) {
        EventDetailDto eventDetail = eventService.getEventById(eventId);
        return ResponseEntity.ok(ApiResponse.success(eventDetail));
    }

    @GetMapping("/front/{eventId}")
    public ResponseEntity<ApiResponse<EventFrontDetailDto>> getEventForFront(@PathVariable Long eventId) {
        return ResponseEntity.ok(ApiResponse.success(eventService.getEventForFront(eventId)));
    }

    @GetMapping("/basic/{eventId}")
    public ResponseEntity<ApiResponse<BasicEventDto>> getBasicEventById(@PathVariable Long eventId) {
        return ResponseEntity.ok(ApiResponse.success(eventService.getBasicEventById(eventId)));
    }

    @GetMapping("/home")
    public ResponseEntity<ApiResponse<Map<String, List<HomeEventSummaryDto>>>> getHomePageEvents() {
        // fix query later
        return ResponseEntity.ok(ApiResponse.success(eventService.getHomePageEvents()));
    }
}