package org.java.bookingservice.feignclient;

import org.java.commonlibrary.model.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "event-service", path = "/api/events")
public interface EventServiceClient {
    record BasicEventDto(Long id, String title, String type, String eventDate, String imageUrl, VenueDto venue) {}
    record VenueDto(String name, String location) {}

    @GetMapping("/basic/{id}")
    ApiResponse<BasicEventDto> getEventById(@PathVariable("id") Long id);
}
