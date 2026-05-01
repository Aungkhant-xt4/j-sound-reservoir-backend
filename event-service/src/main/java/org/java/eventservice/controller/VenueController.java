package org.java.eventservice.controller;

import lombok.RequiredArgsConstructor;
import org.java.commonlibrary.model.dto.ApiResponse;
import org.java.eventservice.model.dto.venue.CreateVenueRequest;
import org.java.eventservice.model.dto.venue.VenueDetailDto;
import org.java.eventservice.model.dto.venue.VenueListDto;
import org.java.eventservice.service.venue.VenueService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> createVenue(@Valid @RequestBody CreateVenueRequest request) {

        venueService.createVenue(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Venue map and details saved successfully."));
    }

    @PutMapping("/{venueId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateVenue(
            @PathVariable Long venueId,
            @Valid @RequestBody CreateVenueRequest request) {

        venueService.updateVenue(venueId, request);

        return ResponseEntity.ok(
                ApiResponse.success("Venue map and details updated successfully.")
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VenueListDto>>> getAllVenues() {

        List<VenueListDto> venues = venueService.getAllVenues();

        return ResponseEntity.ok(ApiResponse.success(venues));
    }

    @GetMapping("/{venueId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VenueDetailDto>> getVenueById(@PathVariable Long venueId) {

        VenueDetailDto venueDetail = venueService.getVenueById(venueId);

        return ResponseEntity.ok(ApiResponse.success(venueDetail));
    }
}
