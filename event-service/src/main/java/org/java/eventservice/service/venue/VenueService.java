package org.java.eventservice.service.venue;

import org.java.eventservice.model.dto.venue.CreateVenueRequest;
import org.java.eventservice.model.dto.venue.VenueDetailDto;
import org.java.eventservice.model.dto.venue.VenueListDto;

import java.util.List;

public interface VenueService {
    void createVenue(CreateVenueRequest request);
    void updateVenue(Long venueId, CreateVenueRequest request);
    List<VenueListDto> getAllVenues();
    VenueDetailDto getVenueById(Long venueId);
}
