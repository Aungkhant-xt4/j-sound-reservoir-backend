package org.java.eventservice.model.dto.event;

public record BasicEventDto(
    Long id, 
    String title, 
    String type, 
    String eventDate, 
    String imageUrl, 
    VenueDto venue
) {
    public record VenueDto(String name, String location) {}
}