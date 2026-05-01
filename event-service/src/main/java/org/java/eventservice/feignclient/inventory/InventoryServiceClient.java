package org.java.eventservice.feignclient.inventory;

import org.java.commonlibrary.model.dto.ApiResponse;
import org.java.eventservice.model.dto.event.BulkSeatCreateRequest;
import org.java.eventservice.model.dto.event.EventDetailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "inventory-service", path = "/api/inventories")
public interface InventoryServiceClient {

    @PostMapping("/seats/bulk")
    ApiResponse<Void> createSeatsInBulk(@RequestBody BulkSeatCreateRequest request);

    @PutMapping("/seats/bulk/sync")
    ApiResponse<Void> updateSeatsInBulk(@RequestBody BulkSeatCreateRequest request);

    @GetMapping("/seats/event/{eventId}")
    ApiResponse<List<EventDetailDto.SeatDto>> getSeatsByEventId(@PathVariable("eventId") Long eventId);
}
