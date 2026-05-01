package org.java.bookingservice.feignclient;

import org.java.bookingservice.model.dto.SeatStatusUpdateRequest;
import org.java.commonlibrary.model.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "inventory-service", path = "/api/inventories")
public interface InventoryServiceClient {

    @PutMapping("/seats/status")
    ApiResponse<Void> updateSeatStatus(@RequestBody SeatStatusUpdateRequest request);

    record SeatDetailDto(String guid, String priceCategoryName, String rowLabel, Integer seatNumber) {}
    @PostMapping("/seats/batch-details")
    ApiResponse<List<SeatDetailDto>> getSeatsByGuids(@RequestBody List<String> guids);
}
